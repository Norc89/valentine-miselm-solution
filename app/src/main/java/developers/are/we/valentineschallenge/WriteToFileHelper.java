package developers.are.we.valentineschallenge;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class WriteToFileHelper {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static void saveToFile(Context context, JSONObject geoJSON) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "geoJson.json");
        String content = geoJSON.toString();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] contentInBytes = content.getBytes();
            fileOutputStream.write(contentInBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(context, "GeoJson created at location :"+file.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
