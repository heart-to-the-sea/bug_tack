const { defineConfig } = require('@vue/cli-service') // V1.2.0
module.exports = defineConfig({
  // V1.2.0
  transpileDependencies: true, // V1.2.0
  devServer: {
    // V1.2.0
    port: 8081, // V1.2.0
    proxy: {
      // V1.2.0
      '/api': {
        // V1.2.0
        target: 'http://localhost:8080', // V1.2.0
        changeOrigin: true, // V1.2.0
        secure: false, // V1.2.0
      }, // V1.2.0
    }, // V1.2.0
  }, // V1.2.0
}) // V1.2.0
