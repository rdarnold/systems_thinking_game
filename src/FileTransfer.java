
package gos;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.SecurityUtils;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream; 
import java.io.FileOutputStream; 
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.EnumSet;
import java.net.URL;
import java.security.PrivateKey;

// To use the service account
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.ByteArrayContent;

// For the uploadProgress property
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

public final class FileTransfer {
    private FileTransfer () { // private constructor
    }

    // Create a property for upload progress so we can tie it to various things.
    private static final DoubleProperty uploadProgress = new SimpleDoubleProperty(0);
    public static DoubleProperty uploadProgressProperty() { return uploadProgress; }
    public static double getUploadProgress() { return uploadProgress.get(); }
    public static void setUploadProgress(double num) { uploadProgress.set(num); }
    public static void resetUploadProgress() { uploadProgress.set(0); }
    public static void addUploadProgress() { addUploadProgress(1); }
    public static void addUploadProgress(double num) {  uploadProgress.set(uploadProgress.get() + num); }
    public static void subUploadProgress() { subUploadProgress(1); }
    public static void subUploadProgress(double num) {
        uploadProgress.set(uploadProgress.get() - num);
        if (uploadProgress.get() < 0) { uploadProgress.set(0); }
    }
    public static boolean uploadComplete() { return (uploadProgress.get() >= 100); }

    
    private static final IntegerProperty uploadSuccess = new SimpleIntegerProperty(0);
    public static IntegerProperty uploadSuccessProperty() { return uploadSuccess; }
    public static void resetUploadSuccess() { uploadSuccess.set(0); }
    public static void setUploadSuccess(int num) { uploadSuccess.set(num); }

    // Application name. 
    private static final String APPLICATION_NAME =
        "Systems Thinking Test";

    // Directory to store user credentials for this application. 
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/systems-thinking-test");

    // Global instance of the {@link FileDataStoreFactory}. 
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    // Global instance of the JSON factory. 
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    // Global instance of the HTTP transport. 
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/systems-thinking-test
     */
    private static final List<String> SCOPES =
        Arrays.asList(DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    /*public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
            FileTransfer.class.getResourceAsStream("/res/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }*/

    /*public Builder setServiceAccountPrivateKeyFromP12File(InputStream p12File)
    throws GeneralSecurityException, IOException {
        serviceAccountPrivateKey = SecurityUtils.loadPrivateKeyFromKeyStore(
        SecurityUtils.getPkcs12KeyStore(), p12File, "notasecret",
        "privatekey", "notasecret");
        return this;
    }*/

    public static Drive getDriveService() throws IOException {
        try {   
            //Credential credential = authorize();
            //GoogleCredential credential = GoogleCredential.fromStream(FileTransfer.class.getResourceAsStream("/res/SystemsThinkingTest_key.json"));
            //java.io.File p12File = new java.io.File("res/SystemsThinkingTest_key.p12");

            //URL url = ClassLoader.getSystemResource("/res/SystemsThinkingTest_key.p12");
            // Turn the resource into a File object
            //Utils.log(url.toURI());
            // It is failing right here
           // java.io.File p12File = new java.io.File(url.toURI());

            // Yay, finally found a way that works from a jar resource
            InputStream in = FileTransfer.class.getResourceAsStream("/res/SystemsThinkingTest_key.p12"); 
            PrivateKey serviceAccountPrivateKey = SecurityUtils.loadPrivateKeyFromKeyStore(
                SecurityUtils.getPkcs12KeyStore(), in, "notasecret", "privatekey", "notasecret");
            
            GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId("systemsthinkingtest@sonorous-nomad-172002.iam.gserviceaccount.com")
                .setServiceAccountScopes(SCOPES)
                //.setServiceAccountPrivateKeyFromP12File(p12File).build();
                .setServiceAccountPrivateKey(serviceAccountPrivateKey).build();

            return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

            /* InputStream credentialsJSON = ServiceAccountCredentials.class.getClassLoader()
            .getResourceAsStream("/res/SystemsThinkingTest_key.json");

            GoogleCredential gcFromJson = GoogleCredential.fromStream(credentialsJSON, httpTransport, jsonFactory).createScoped(scopes);

            return new GoogleCredential.Builder()
                .setTransport(gcFromJson.getTransport())
                .setJsonFactory(gcFromJson.getJsonFactory())
                .setServiceAccountId(gcFromJson.getServiceAccountId())
                .setServiceAccountUser("systemsthinkingtest@sonorous-nomad-172002.iam.gserviceaccount.com")
                .setServiceAccountPrivateKey(gcFromJson.getServiceAccountPrivateKey())
                .setServiceAccountScopes(gcFromJson.getServiceAccountScopes())
                .build();*/
        }
        catch (Exception e) {
            // Fuck you
            Utils.log("An error occurred: " + e.toString());
        }
        return null;        
    }

    /*
    $mime_types= array(
    "xls" =>'application/vnd.ms-excel',
    "xlsx" =>'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    "xml" =>'text/xml',
    "ods"=>'application/vnd.oasis.opendocument.spreadsheet',
    "csv"=>'text/plain',
    "tmpl"=>'text/plain',
    "pdf"=> 'application/pdf',
    "php"=>'application/x-httpd-php',
    "jpg"=>'image/jpeg',
    "png"=>'image/png',
    "gif"=>'image/gif',
    "bmp"=>'image/bmp',
    "txt"=>'text/plain',
    "doc"=>'application/msword',
    "js"=>'text/js',
    "swf"=>'application/x-shockwave-flash',
    "mp3"=>'audio/mpeg',
    "zip"=>'application/zip',
    "rar"=>'application/rar',
    "tar"=>'application/tar',
    "arj"=>'application/arj',
    "cab"=>'application/cab',
    "html"=>'text/html',
    "htm"=>'text/html',
    "default"=>'application/octet-stream',
    "folder"=>'application/vnd.google-apps.folder';*/

    // This actually calls into the Uploader class, which calls
    // back into this class to do the upload.  Little strange but
    // works fine.
    public static void runUploadThread() {
        resetUploadSuccess();
        resetUploadProgress();
        Utils.log("Starting upload...");
        Uploader uploader = new Uploader(); //Player.getPlayerData());
        Thread t = new Thread(uploader);
        setUploadProgress(.1);
        t.start();

        // Could also just do this without the class;
        // new Thread() { public void run() { FileTransfer.uploadData(); } }.start(); 
    }

    public static boolean uploadData() {
        try {
            Drive service = getDriveService();
            setUploadProgress(.2);
            Player.lastSavedData = Player.getPlayerData();
            Utils.log(Player.getDataSizeString(Player.lastSavedData));
            if (createFileFromString(service, Player.getDataFileName(), 
                    "Simulation Results", "text/plain;charset=UTF-8", Player.lastSavedData) != null) {
                setUploadProgress(1);
                setUploadSuccess(1);
                Player.incSaveNum(); // Increment the number of saves we've done during the game
                return true;
            } 
            //createFileFromFile(service, "Test File Zoids.txt", "Simulation Results", "text/plain", "TestFileZoids.txt");
        } 
        catch (IOException e) {
            Utils.log("uploadData: An error occurred: " + e.toString());
        }
        // Fail
        setUploadSuccess(2);
        return false;
    }

    private static File createFileFromString(Drive service, String name, String description,
        String mimeType, String contents) 
    {
        // File's metadata.
        File body = new File();
        body.setName(name);
        body.setDescription(description);
        body.setMimeType(mimeType);

        // Set the parent folder.  
        // It is the SimulationResults folder but then we create one based on the ID of the
        // player.
        // Get the Id fo the folder by clicking on "Get shareable link" after clicking into
        // the folder in Google Drive and clicking the little down arrow next to the folder name.
        String parentFolderId = "0B9qbayJt45sFV3FkaTJxeWZQRnM";  
        body.setParents(Arrays.asList(parentFolderId));

        // File's content.
        ByteArrayContent mediaContent = new ByteArrayContent(mimeType, contents.getBytes());
        setUploadProgress(.3);
        if (service == null) {
            Utils.log("Service was NULL, unable to create file");
        }
        try {
            File file = service.files().create(body, mediaContent).execute();
            setUploadProgress(.7);

            System.out.println("File created: " + file.getName() + " (" + file.getId() + ")");
            // Actually setPermissions does work, but I don't need to do it anymore because I'm
            // uploading directly to a shared folder in the main account.  If I wasn't doing
            // that, I would need to set permissions to share the file with the other accounts
            // I wanted to see it.
            //setPermission(service, file.getId());
            setUploadProgress(.8);

            return file;
        } 
        catch (IOException e) {
            Utils.log("An error occurred: " + e.toString());
            return null;
        }
    }

    /*private static File createFileFromFile(Drive service, String name, String description,
        String mimeType, String filename) 
    {
        // File's metadata.
        File body = new File();
        body.setName(name);
        body.setDescription(description);
        body.setMimeType(mimeType);

        String parentFolderId = "0B9qbayJt45sFV3FkaTJxeWZQRnM";  
        body.setParents(Arrays.asList(parentFolderId));
        // Set the parent folder.
        //if (parentId != null && parentId.length() > 0) {
        //body.setParents(
          //  Arrays.asList(new ParentReference().setId(parentId)));
        //}

        // File's content.
        java.io.File fileContent = new java.io.File(filename);
        FileContent mediaContent = new FileContent(mimeType, fileContent);
        try {
            File file = service.files().create(body, mediaContent).execute();
            System.out.println("File created: " + file.getName() + " (" + file.getId() + ")");
            setPermission(service, file.getId());
            return file;
        } 
        catch (IOException e) {
            Utils.log("An error occurred: " + e.toString());
            return null;
        }
    }*/

    /**
     * Insert a new permission.
     *
     * @param service Drive API service instance.
     * @param fileId ID of the file to insert permission for.
     * @param value User or group e-mail address, domain name or {@code null}
                     "default" type.
     * @param type The value "user", "group", "domain" or "default".
     * @param role The value "owner", "writer" or "reader".
     * @return The inserted permission if successful, {@code null} otherwise.
     */
    private static Permission setPermission(Drive service, String fileId) {
        return setPermission(service, fileId, "systemsthinkingtest@gmail.com", "user", "owner", 0);
    }

    private static Permission setPermission(Drive service, String fileId,
        String email, String type, String role, int times) 
    {
        // Ok so pause a little before we set permission because the file might not even be uploaded or
        // ready to use yet.  Actually we should do some check to see if it's ready.  Or we should
        // just sleep and try again if we get a 500 error, or do some back-off thing where we keep
        // trying every few seconds.

        // If we have retried 10 times we've spent almost a minute on this.  Time to call it quits.
        if (times > 10) {
            System.out.println("Unable to set permissions on file after 10 retries: " + fileId);
            return null;
        }

        // Actually just give it a few seconds to sleep the first time, because it seems to often
        // mess up without it.  Then we do a 5 second looping retry until we get it or we just try
        // too many times.
        try {
            Thread.sleep(3000);
        } 
        catch (Exception e) {
            System.out.println("Unable to sleep: " + e);
        }

        Permission userPermission = new Permission()
            .setType(type)
            .setRole(role)
            .setEmailAddress(email);

        try {
            return service.permissions().create(fileId, userPermission).execute();
            /*return service.permissions().create(fileId, userPermission)
            .setFields("id")
            .queue(batch, callback);*/
        } 
        catch (IOException e) {
            System.out.println("An error occurred: " + e);
            System.out.println("Retrying in 5 seconds.");
            try {
                Thread.sleep(5000);
            } 
            catch (Exception efx) {
                System.out.println("Unable to sleep: " + efx);
            }
            return setPermission(service, fileId, email, type, role, times+1);
        }
        //return null;
    }

    public static void deleteFiles() {
        try {
            // Build a new authorized API client service.
            Drive service = getDriveService();
            //service.files().delete("0B4p2TcOSfukmVk9tNGQybzNncEE").execute();

            // Now take a look at what we have left
            viewFiles();
        } 
        catch (IOException e) {
            System.out.println(e.toString());
        }
    }
    
    public static void viewFiles() {
        try {
            // Build a new authorized API client service.
            Drive service = getDriveService();

            // Print the names and IDs for up to 200 files.
            FileList result = service.files().list()
                .setPageSize(200)
                .setFields("nextPageToken, files(id, name)")
                .execute();
            List<File> files = result.getFiles();
            if (files == null || files.size() == 0) {
                System.out.println("No files found.");
            } else {
                System.out.println("Files:");
                for (File file : files) {
                    System.out.printf("%s (%s)\n", file.getName(), file.getId());
                }
            }
        } 
        catch (IOException e) {
            System.out.println(e.toString());
        }
    }

}

/*
import java.util.EnumSet;
import java.util.ArrayList;
import java.util.List;
import java.io.*; 

// Service account ID: systemsthinkingtest@sonorous-nomad-172002.iam.gserviceaccount.com
// Service account key ID: a262e81b43152a9b3edb2c2fb09b5a321f4c4e09

// P12 password: notasecret
// P12 key ID: 99587a8112be1cb6fb714021b8bd80b77a3414fd

// Client ID: 201848521226-2infct2h0uousbakujak3too55vkh1a5.apps.googleusercontent.com
// Client secret: qA4NvbJ_JkgUnvqRjlOiIskC

public final class FileTransfer {
    private FileTransfer () { // private constructor
    }

    public void sendData() {
        File fileMetadata = new File();
        fileMetadata.setName("photo.jpg");
        java.io.File filePath = new java.io.File("files/photo.jpg");
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        System.out.println("File ID: " + file.getId());
    }
}*/