# Use Node 22 to match your modern npm version
FROM node:22-alpine
WORKDIR /app

# Copy package files
COPY package*.json ./
RUN npm install

# Copy the rest of the UI code
COPY . .

# Vite default port
EXPOSE 5173

# The '--host' flag is REQUIRED for Vite to work inside Docker
CMD ["npm", "run", "dev", "--", "--host"]