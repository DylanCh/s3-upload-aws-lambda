package com.cox;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cox.Models.S3UploadRequestModel;
import com.cox.Models.S3UploadResponseModel;

import java.util.Base64;
import java.io.*;

public class S3UploadFunction implements RequestHandler<S3UploadRequestModel, S3UploadResponseModel> {


    public S3UploadResponseModel handleRequest(S3UploadRequestModel model, Context context) {
        context.getLogger().log("Request is " + ((model.isPublic()) ? " public " : "not public"));
        String message = null;
        String url = null;
        S3UploadResponseModel responseModel=new S3UploadResponseModel();
        try {
            byte[] bytes = Base64.getDecoder().decode(new String(model.getFileAsBased64String()).getBytes("UTF-8"));
            InputStream stream = new ByteArrayInputStream(bytes);
            AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .build();
            ObjectMetadata metadata = new ObjectMetadata(){{
                setContentLength(bytes.length);
            }};
            PutObjectRequest putObjectRequest = new PutObjectRequest(Global.BUCKET_NAME,model.getFileName(),stream, metadata);
            if(model.isPublic()){
                putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            }
            s3client.putObject(putObjectRequest);
            message = "File uploaded";
            if(model.isPublic()) {
                try {
                    url = s3client.getUrl(Global.BUCKET_NAME, model.getFileName()).toString();
                    if (url != null) {
                        context.getLogger().log(url);
                        responseModel.setUrl(url);
                    }
                } catch (Exception e) {
                    context.getLogger().log(e.getMessage());
                }
            }
        } catch (UnsupportedEncodingException e) {
            message = "File encoding not supported";
        }
        catch (AmazonServiceException ase) {
            message = new StringBuilder("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.").append("Error Message:    " + ase.getMessage())
                    .append("HTTP Status Code: " + ase.getStatusCode()).toString();

        } catch (AmazonClientException ace) {
            message = new StringBuilder("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.")
            .append("Error Message: " + ace.getMessage()).toString();
        }
        responseModel.setMessage(message);
        return responseModel;
    }
}
