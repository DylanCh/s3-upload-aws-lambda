package com.cox.Models;

public class S3UploadRequestModel {
    private String fileAsBased64String, fileName;
    private boolean isPublic;

    public String getFileAsBased64String() {
        return fileAsBased64String;
    }

    public void setFileAsBased64String(String fileAsBased64String) {
        this.fileAsBased64String = fileAsBased64String;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
