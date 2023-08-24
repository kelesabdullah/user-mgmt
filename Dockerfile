# Base image - #node:18
FROM node:alpine

# Create app directory
WORKDIR /app

# A wildcard is used to ensure both package.json AND package-lock.json are copied
COPY package*.json ./

# Install app dependencies
RUN npm install

# Bundle app source
COPY . .

# Creates a "dist" folder with the production build
RUN npm run build

# Set NODE_ENV environment variable
ENV NODE_ENV development

# Start the server using the production build
CMD [ "node", "dist/main.js" ]