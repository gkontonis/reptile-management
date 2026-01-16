const PROXY_CONFIG = {
  "/api/**": {
    target: "http://localhost:8081",
    secure: false,
    changeOrigin: true,
    logLevel: "debug",
    onProxyReq: function (proxyReq, req, res) {
      console.log('[Proxy] Forwarding:', req.method, req.url, '→', proxyReq.path);
    },
    onProxyRes: function (proxyRes, req, res) {
      console.log('[Proxy] Response:', proxyRes.statusCode, 'for', req.url);
    },
    onError: function (err, req, res) {
      console.error('[Proxy] Error:', err.message, 'for', req.url);
    }
  }
};

module.exports = PROXY_CONFIG;
