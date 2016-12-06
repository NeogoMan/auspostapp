package com.conducthq.auspost.model.response;

/**
 * Created by conduct19 on 27/10/2016.
 */

public class ImageUploadResponse
{
    ImageData[] data;
    Boolean success;
    Boolean error;

    public String getImageUrl() {
        // for now, we just choose the LARGEST image (in pixel area)
        int max = 0;
        String largest = null;
        if (data != null) {
            for (ImageData d : data) {
                if (d.width * d.height > max) {
                    max = d.width * d.height;
                    largest = d.path;
                }
            }
        }
        return largest;
    }

    public String getThumbImageUrl() {
        // for now, we just choose the SMALLEST image (in pixel area)
        int min = Integer.MAX_VALUE;
        String smallest = null;
        if (data != null) {
            for (ImageData d : data) {
                if (d.width * d.height < min) {
                    min = d.width * d.height;
                    smallest = d.path;
                }
            }
        }
        return smallest;
    }

    public static class ImageData {
        String size;  // eg. "medium"
        String path;
        int width, height;
    }
}
