# Session Debugging Guide

## Problem
- ✅ Login works (status 200)
- ❌ Students API returns 401 Unauthorized
- Issue: Session not being transferred to API calls

## Debugging Steps

### Step 1: Check Authentication Status
After login, test this endpoint to see if you're authenticated:

```
GET https://school-management-c5dg.onrender.com/api/debug/auth-status
```

**Expected Response (Authenticated):**
```json
{
  "authenticated": true,
  "principal": "your_username",
  "authorities": "[ROLE_USER]"
}
```

**If you see:**
```json
{
  "authenticated": false,
  "principal": "anonymousUser",
  "authorities": "[ROLE_ANONYMOUS]"
}
```

Then the session is NOT being persisted.

### Step 2: Check Browser Cookies
After login, check if session cookie exists:

1. Open DevTools (F12)
2. Go to **Application** tab
3. Go to **Cookies** → `https://school-management-c5dg.onrender.com`
4. Look for cookie named: **JSESSIONID**

**If JSESSIONID exists:**
- Session cookie is being set ✅
- Check if it's being sent in API requests

**If JSESSIONID does NOT exist:**
- Session cookie is not being created ❌
- This is the root cause

### Step 3: Verify Cookie is Sent with API Requests
1. Open DevTools Network tab
2. Make an API call (click to load students)
3. Find the request: `GET /api/students`
4. Click on it and go to **Headers** tab
5. Scroll down to **Request Headers**
6. Look for: `Cookie: JSESSIONID=...`

**If found:** Cookie is being sent ✅
**If NOT found:** Frontend not sending credentials ❌

### Step 4: Check Render Logs
Go to your Render service logs:
1. Visit: https://dashboard.render.com
2. Select your backend service
3. Click **Logs** tab
4. Look for our debug output:

```
=== GET /api/students ===
Authentication: your_username (or anonymousUser)
Authenticated: true (or false)
Authorities: [ROLE_USER]
========================
```

This will tell you if Spring is recognizing the session.

## Common Issues and Solutions

### Issue 1: JSESSIONID Cookie Exists but 401 Error
**Problem:** Cookie is created but not recognized on API calls

**Check:**
- Is `withCredentials: true` in frontend API config? (YES ✅)
- Is CORS `allowCredentials = "true"`? (YES ✅)
- Check if cookie domain/path is correct in DevTools

**Solution:**
- Check `same-site` and `secure` cookie settings

### Issue 2: JSESSIONID Cookie NOT Created After Login
**Problem:** Session not being created

**Check:**
- Login response status is 200 ✅
- No Set-Cookie header in login response?

**Solution:**
- Check if JDBC session table exists:
  ```sql
  SELECT * FROM SPRING_SESSION;
  ```
- If table doesn't exist, JDBC sessions aren't initialized properly

### Issue 3: Session Lost Between Requests
**Problem:** JSESSIONID exists but changes between requests

**Check:**
- Each request should have the SAME JSESSIONID

**Solution:**
- Check if session table is being properly queried

## Quick Test with cURL

```bash
# 1. Login and capture cookies
curl -X POST "https://school-management-c5dg.onrender.com/api/public/login" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin&password=admin" \
  -c cookies.txt \
  -v

# 2. Use saved cookies to request students
curl -X GET "https://school-management-c5dg.onrender.com/api/students" \
  -b cookies.txt \
  -v

# 3. Check auth status with cookies
curl -X GET "https://school-management-c5dg.onrender.com/api/debug/auth-status" \
  -b cookies.txt \
  -v
```

## Next Steps

1. **First, run the debugging endpoint test**
   - Test: GET `/api/debug/auth-status` after login
   - Check response

2. **If authenticated shows `true`:**
   - Issue is in the frontend OR cookie transfer
   - Check if cookies are being sent

3. **If authenticated shows `false`:**
   - Session is not persisting
   - Check Render logs for errors
   - Verify database connection
   - Check if SPRING_SESSION table exists

## Files to Check

- `application.properties` - Session config
- `SecurityConfig.java` - Session creation policy
- `api.js` - Frontend axios config (withCredentials)
- Render logs - Backend errors

