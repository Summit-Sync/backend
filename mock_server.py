from http.server import BaseHTTPRequestHandler, HTTPServer
from urllib.parse import urlparse, parse_qs
import requests


class SimpleRequestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        self.handle_request()

    def do_POST(self):
        self.handle_request()

    def handle_request(self):
        # Parse the URL
        parsed_url = urlparse(self.path)
        query_params = parse_qs(parsed_url.query)

        # Read the request body for POST requests
        content_length = int(self.headers.get('Content-Length', 0))
        body = self.rfile.read(content_length).decode('utf-8')

        # Print the URL, body, and query parameters
        #print(f"URL: {self.path}")
        #print(f"Body: {body}")
        #print(f"Query Parameters: {query_params}")

        self.send_response(200)
        self.send_header('Content-Type', 'text/plain')
        self.end_headers()
        self.wfile.write(b'Success')
        res = requests.get('http://localhost:8080/auth/callback', params={'state':query_params['state'], 'code': query_params['code']})
        print(res.headers)
        print(res.status_code)
        print(res.text)

httpd = HTTPServer(('', 8081), SimpleRequestHandler)

try:
    # Start the server
    httpd.serve_forever()
except KeyboardInterrupt:
    # Handle Ctrl+C to gracefully shut down the server
    print("\nShutting down the server...")
    httpd.server_close()
