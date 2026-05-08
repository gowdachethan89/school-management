# Cross-Domain Session Fix - Testing Guide

## What Changed

The issue was that session cookies from Render backend were not being preserved when calling from Vercel frontend (different domains).

**Solution implemented:**
1. Backend now returns session ID in login response AND as response header
2. Frontend captures session ID and sends it with every subsequent request
3. Backend accepts session ID from `X-JSESSIONID` header as a fallback to cookies

## Deployment Steps

1. **Commit all changes:**
```bash
git add .
git commit -m "Implement cross-domain session handling with header-based fallback"
git push
```

2. **Wait for Render auto-redeploy** (~2-5 minutes)

3. **Wait for Vercel auto-redeploy** (~1-2 minutes)

## Testing Steps

### Step 1: Clear Browser Data
Before testing, clear all cookies and local storage:
1. Open DevTools (F12)
2. Go to **Application** tab
3. Clear **Cookies** for both domains
4. Clear **Local Storage**
5. Close and reopen the browser

### Step 2: Test Login and Capture Session ID
1. Visit: https://school-management-one-beryl.vercel.app
2. Login with your credentials
3. Open DevTools (F12) → **Network** tab
4. Find the **login** POST request
5. Click it and check:
   - **Response** tab: Should see `"sessionId": "..."` field
   - **Headers** tab: Look for response header `X-JSESSIONID: ...`
   - **Storage** tab → **Local Storage**: Should see `sessionId` stored

### Step 3: Verify Session ID is Sent with API Requests
1. Still in DevTools **Network** tab
2. Try to load the dashboard (wait for students list to load)
3. Find the first **students** GET request that was made automatically
4. Click it and check **Headers** tab:
   - **Request Headers**: Should see `X-JSESSIONID: ...`
   - **Request Headers**: Should also see `Cookie: JSESSIONID=...` (if cookies work)

### Step 4: Check Debug Endpoint
After login, test the auth status endpoint:
1. Open new tab and visit:
   ```
   https://school-management-c5dg.onrender.com/api/debug/auth-status
   ```
2. Expected response (authenticated):
   ```json
   {
     "authenticated": true,
     "principal": "your_username",
     "authorities": "[ROLE_USER]"
   }
   ```

**If still shows `anonymousUser`:**
- Go to Render logs and check for our debug messages
- Look for: `=== LOGIN SUCCESS ===` logs

### Step 5: Check Render Logs
1. Visit https://dashboard.render.com
2. Select your backend service
3. Click **Logs** tab
4. Look for these debug messages:

**After login:**
```
=== LOGIN SUCCESS ===
Username: your_username
Session ID: 1234567890ABCDEF
====================
```

**After students API call:**
```
=== GET /api/students ===
Authentication: your_username (NOT anonymousUser!)
Authenticated: true
Authorities: [ROLE_USER]
========================
```

## Expected Behavior

✅ **Success Scenario:**
- Login response includes `sessionId` field
- Console shows "Session ID stored: ..."
- Students list loads and shows data
- Dashboard displays correctly
- No 401 errors

❌ **If Still Seeing 401 Errors:**

1. **First check:** Are headers being sent?
   - DevTools → Network → students request → Headers
   - Look for `X-JSESSIONID` header
   - If missing, frontend capture isn't working

2. **Second check:** Backend receiving header?
   - Check Render logs for session ID confirmation
   - Look for: `Applied X-JSESSIONID header as cookie: ...`
   - If not there, the filter might not be active

3. **Third check:** Session validity?
   - After login, immediately test `/api/students`
   - If it fails after a while, sessions might be expiring
   - Default session timeout is usually 30 minutes

## Browser Console Output - What to Expect

After login, your browser console should show:
```
Session ID stored: 1234567890ABCDEF
```

Additional debug info will be visible in Network tab headers.

## If Everything Still Doesn't Work

### Option 1: Check Local Environment First
```bash
# Test locally (if possible)
cd my-school-ui
npm run dev
# Visit http://localhost:5173
# Login and test
```

### Option 2: Check Database
Render dashboard → select database → query students table:
```sql
SELECT * FROM student;
SELECT * FROM SPRING_SESSION;
```

If `SPRING_SESSION` table is empty, sessions aren't being persisted.

### Option 3: Check Render Environment Variables
Make sure `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` are correctly set.

## Debugging Checklist

- [ ] All code changes deployed to Render and Vercel
- [ ] Browser local storage cleared
- [ ] Browser cookies cleared
- [ ] Login succeeds and shows session ID
- [ ] Session ID stored in local storage (check DevTools)
- [ ] Session ID sent as header in students request (check Network)
- [ ] Students API returns 200 with data
- [ ] Dashboard shows student list

## Files Modified

- `school-backend/src/main/java/.../LoginSuccessHandler.java` - Returns session ID
- `school-backend/src/main/java/.../CorsSessionFilter.java` - NEW: Sets CORS headers
- `school-backend/src/main/java/.../SessionHeaderFilter.java` - NEW: Accepts X-JSESSIONID header
- `school-backend/src/main/java/.../DebugController.java` - NEW: Debug endpoint
- `my-school-ui/src/services/api.js` - Captures and sends session ID
- `my-school-ui/src/context/AuthContext.jsx` - Clears session ID on logout

## Next Steps After Deployment

1. Deploy both backends
2. Clear browser data
3. Test login
4. Check console for "Session ID stored" message
5. Report the debug endpoint response: `/api/debug/auth-status`
6. Check what appears in Render logs

This hybrid approach gives us both cookie-based sessions (for same-domain clients) and header-based sessions (for cross-domain clients like your Vercel frontend).

