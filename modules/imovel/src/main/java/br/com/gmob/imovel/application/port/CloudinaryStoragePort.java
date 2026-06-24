package br.com.gmob.imovel.application.port;

public interface CloudinaryStoragePort {

    String uploadImage(byte[] fileBytes, String originalFilename);
}
