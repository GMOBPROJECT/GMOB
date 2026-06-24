package br.com.gmob.imovel.infrastructure.cloudinary;

import br.com.gmob.imovel.application.port.CloudinaryStoragePort;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryStorageAdapter implements CloudinaryStoragePort {

    private final Cloudinary cloudinary;

    public CloudinaryStorageAdapter(CloudinaryProperties properties) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", properties.cloudName(),
                "api_key", properties.apiKey(),
                "api_secret", properties.apiSecret()
        ));
    }

    @Override
    @SuppressWarnings("unchecked")
    public String uploadImage(byte[] fileBytes, String originalFilename) {
        try {
            Map<String, Object> result = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
                    "folder", "imoveis",
                    "resource_type", "image"
            ));
            return (String) result.get("secure_url");
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao enviar imagem para o Cloudinary", ex);
        }
    }
}
