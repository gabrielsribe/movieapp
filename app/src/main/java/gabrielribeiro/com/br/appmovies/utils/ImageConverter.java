package gabrielribeiro.com.br.appmovies.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImageConverter {

    public static byte[] ConvertImageToByteArray(Bitmap Image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.JPEG,100, baos);
        return baos.toByteArray();

    }

    public static Bitmap ConvertByteArrayToBitmap (byte[] byteArary){
        return BitmapFactory.decodeByteArray(byteArary, 0, byteArary.length);
    }

}
