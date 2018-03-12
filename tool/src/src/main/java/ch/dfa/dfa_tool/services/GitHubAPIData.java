package ch.dfa.dfa_tool.services;

import ch.dfa.dfa_tool.models.GitHubAPIMetaData;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Created by salizumberi-laptop on 19.10.2016.
 */
public class GitHubAPIData {

    static public String User = null;
    static public String PW = null;


    public static GitHubAPIMetaData getGitHubRepository(String path){
        Gson gson = new Gson();
        GitHubAPIMetaData repository = gson.fromJson(getJsonRepository(path).toString(), GitHubAPIMetaData.class);
        return repository;
    }


    public static JSONObject getJsonRepository(String path){
        JSONObject json = null;
        try {
            json = readJsonFromUrl2(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
           InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);

            return json;
        } finally {
            is.close();
        }
    }

    public static JSONObject readJsonFromUrl2(String get) throws IOException, JSONException {
        URL url = new URL(get);
        String username = User;
        String password = PW;
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(30000); // 30 seconds time out

       // CsvListWriter errorwriter = new CsvListWriter(new FileWriter(new File("bigquerydata\\errorlog.csv"),true), CsvPreference.STANDARD_PREFERENCE);

        if (username != null && password != null){
            System.out.println("Actual User (Request) :" + User);
            String user_pass = username + ":" + password;
            String encoded = Base64.encodeBase64String( user_pass.getBytes() );
            conn.setRequestProperty("Authorization", "Basic " + encoded);
        }

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            rd.close();
            return json;
        } finally {
       //     errorwriter.write(get);
            conn.getInputStream().close();
       //     errorwriter.close();

        }
    }

    public static String getHttpResponse(String address, String username, String password) throws Exception {
        URL url = new URL(address);
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(30000); // 30 seconds time out

        if (username != null && password != null){
            String user_pass = username + ":" + password;
            String encoded = Base64.encodeBase64String( user_pass.getBytes() );
            conn.setRequestProperty("Authorization", "Basic " + encoded);
        }

        String line = "";
        StringBuffer sb = new StringBuffer();
        BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()) );
        while((line = input.readLine()) != null)
            sb.append(line);
        input.close();
        return sb.toString();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


}
