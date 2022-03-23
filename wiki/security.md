###Protocol
All calls are HTTPS , if any HTTP call is made it is automatically redirected to HTTPS.

###Authentication
Service is accessible only within MIQ internal network. Authentication is handled at API gateway level.Each client is given a secret key to access APIs