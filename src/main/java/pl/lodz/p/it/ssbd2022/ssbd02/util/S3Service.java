package pl.lodz.p.it.ssbd2022.ssbd02.util;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotoFoundException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Singleton;
import javax.inject.Inject;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.addPhotoToGallery;

@Singleton
public class S3Service {

    private static final Region awsRegion = Region.EU_CENTRAL_1;
    private static final String bucketName = "ssbd02";
    private S3Client s3;

    @Inject
    private ConfigLoader configLoader;

    @PostConstruct
    public void init() {
        this.s3 = S3Client.builder()
                .region(awsRegion)
                .credentialsProvider(() -> AwsBasicCredentials.create(
                        configLoader.getAwsAccessKeyId(),
                        configLoader.getAwsSecretAccessKey())
                )
                .build();
    }

    /**
     * Umieszcza w serwisie AWS S3 zdjęcie w formacie png o podanych danych oraz zwraca odnośnik do niego
     *
     * @param owner      właściciel zdjęcia
     * @param data       dane zdjęcia w postaci ciągu bajtów
     * @param objectName nazwa obiektu
     * @return odnośnik do umieszczonego w serwisie AWS S3 zdjęcia
     */
    @RolesAllowed(addPhotoToGallery)
    public String uploadObject(String owner, byte[] data, String objectName) {
        String objectKey = owner + "/" + objectName + ".png";
        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .contentType("x-png")
                        .contentLength((long) data.length)
                        .build(),
                RequestBody.fromBytes(data)
        );

        String httpsUrl = s3.utilities().getUrl(GetUrlRequest.builder()
                        .bucket(bucketName)
                        .region(awsRegion)
                        .key(objectKey)
                        .build())
                .toString();

        return httpsUrl.replaceAll("https", "http");
    }

    /**
     * Usuwa z serwisu AWS S3 zdjęcie należące do podanego użytkownika o podanym kluczu
     *
     * @param owner     nazwa użytkownika, którego zdjęcie ma zostać usunięte
     * @param objectKey klucz zdjęcia, które ma zostać usunięte
     * @throws NoPhotoFoundException zdjęcie o danym kluczu / właścicielu nie istnieje
     */
    public void deleteObject(String owner, String objectKey) throws NoPhotoFoundException {
        String key = owner + "/" + objectKey + ".png";
        try {
            s3.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build()
            );
        } catch (NoSuchKeyException e) {
            throw ExceptionFactory.noPhotoFoundException();
        }
    }
}
