package Service;

import com.cloudinary.Cloudinary;
import jakarta.servlet.http.Part;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UploadService {

    private final Cloudinary cloudinary;
    public UploadService() {
        cloudinary = new Cloudinary(Map.of(
                "cloud_name", "dyw6k0pz6",
                "api_key", "481496538886471",
                "api_secret", "TZz2KR2Z1ohIvOsE5q4_FOpiUk4"
        ));
    }

    public String upload(Part file, String folder) {
        try {
            if (file == null || file.getSize() == 0) return null;
            byte[] bytes = file.getInputStream().readAllBytes();
            Map res = cloudinary.uploader().upload(
                    bytes,
                    Map.of("folder", folder)
            );

            return (String) res.get("secure_url");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> uploadMultiple(List<Part> files, String folder) {
        List<String> urls = new ArrayList<>();
        for (Part p : files) {
            urls.add(upload(p, folder));
        }
        return urls;
    }
}

