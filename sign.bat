cd build
jarsigner -tsa http://timestamp.comodoca.com -storetype pkcs12 -keystore ../certs/ra_sectigo.p12 -storepass **SystemsThinkingGame12 SystemsThinkingGame.jar "ra_id"
cd ..


