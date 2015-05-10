package com.fmc.edu.http;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.async.parser.AsyncParser;
import com.koushikdutta.ion.HeadersCallback;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.bitmap.BitmapInfo;
import com.koushikdutta.ion.bitmap.LocallyCachedStatus;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.builder.FutureBuilder;
import com.koushikdutta.ion.future.ImageViewFuture;
import com.koushikdutta.ion.future.ResponseFuture;

import org.apache.http.NameValuePair;
import org.w3c.dom.Document;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/10.
 */
public class MyIconB implements Builders.Any.B {

    public Builders.Any.U setBase64BodyParameter(String name, String value) {
        return this.setBodyParameter(name, Base64.encodeToString(value.getBytes(), Base64.DEFAULT));
    }

    @Override
    public ResponseFuture<String> asString() {
        return null;
    }

    @Override
    public ResponseFuture<String> asString(Charset charset) {
        return null;
    }

    @Override
    public ResponseFuture<InputStream> asInputStream() {
        return null;
    }

    @Override
    public ResponseFuture<Document> asDocument() {
        return null;
    }

    @Override
    public Builders.Any.BF<? extends Builders.Any.BF<?>> withBitmap() {
        return null;
    }

    @Override
    public <T extends OutputStream> ResponseFuture<T> write(T outputStream) {
        return null;
    }

    @Override
    public <T extends OutputStream> ResponseFuture<T> write(T outputStream, boolean close) {
        return null;
    }

    @Override
    public ResponseFuture<File> write(File file) {
        return null;
    }

    @Override
    public <T> ResponseFuture<T> as(AsyncParser<T> parser) {
        return null;
    }

    @Override
    public ResponseFuture<byte[]> asByteArray() {
        return null;
    }

    @Override
    public FutureBuilder group(Object groupKey) {
        return null;
    }

    @Override
    public Future<Bitmap> asBitmap() {
        return null;
    }

    @Override
    public BitmapInfo asCachedBitmap() {
        return null;
    }

    @Override
    public LocallyCachedStatus isLocallyCached() {
        return null;
    }

    @Override
    public ResponseFuture<JsonArray> asJsonArray() {
        return null;
    }

    @Override
    public ResponseFuture<JsonObject> asJsonObject() {
        return null;
    }

    @Override
    public ResponseFuture<JsonArray> asJsonArray(Charset charset) {
        return null;
    }

    @Override
    public ResponseFuture<JsonObject> asJsonObject(Charset charset) {
        return null;
    }

    @Override
    public <T> ResponseFuture<T> as(Class<T> clazz) {
        return null;
    }

    @Override
    public <T> ResponseFuture<T> as(TypeToken<T> token) {
        return null;
    }

    @Override
    public ImageViewFuture intoImageView(ImageView imageView) {
        return null;
    }

    @Override
    public Builders.Any.B setLogging(String tag, int level) {
        return null;
    }

    @Override
    public Builders.Any.B proxy(String host, int port) {
        return null;
    }

    @Override
    public Builders.Any.B progress(ProgressCallback callback) {
        return null;
    }

    @Override
    public Builders.Any.B progressHandler(ProgressCallback callback) {
        return null;
    }

    @Override
    public Builders.Any.B progressBar(ProgressBar progressBar) {
        return null;
    }

    @Override
    public Builders.Any.B progressDialog(ProgressDialog progressDialog) {
        return null;
    }

    @Override
    public Builders.Any.B uploadProgress(ProgressCallback callback) {
        return null;
    }

    @Override
    public Builders.Any.B uploadProgressHandler(ProgressCallback callback) {
        return null;
    }

    @Override
    public Builders.Any.B uploadProgressBar(ProgressBar progressBar) {
        return null;
    }

    @Override
    public Builders.Any.B uploadProgressDialog(ProgressDialog progressDialog) {
        return null;
    }

    @Override
    public Builders.Any.B setHandler(Handler handler) {
        return null;
    }

    @Override
    public Builders.Any.B setHeader(String name, String value) {
        return null;
    }

    @Override
    public Builders.Any.B setHeader(NameValuePair... header) {
        return null;
    }

    @Override
    public Builders.Any.B noCache() {
        return null;
    }

    @Override
    public Builders.Any.B followRedirect(boolean follow) {
        return null;
    }

    @Override
    public Builders.Any.B addHeader(String name, String value) {
        return null;
    }

    @Override
    public Builders.Any.B addHeaders(Map<String, List<String>> params) {
        return null;
    }

    @Override
    public Builders.Any.B addQuery(String name, String value) {
        return null;
    }

    @Override
    public Builders.Any.B addQueries(Map<String, List<String>> params) {
        return null;
    }

    @Override
    public Builders.Any.B userAgent(String userAgent) {
        return null;
    }

    @Override
    public Builders.Any.B setTimeout(int timeoutMilliseconds) {
        return null;
    }

    @Override
    public Builders.Any.B onHeaders(HeadersCallback callback) {
        return null;
    }

    @Override
    public Builders.Any.B basicAuthentication(String username, String password) {
        return null;
    }

    @Override
    public Builders.Any.F setJsonObjectBody(JsonObject jsonObject) {
        return null;
    }

    @Override
    public <T> Builders.Any.F setJsonPojoBody(T object, TypeToken<T> token) {
        return null;
    }

    @Override
    public <T> Builders.Any.F setJsonPojoBody(T object) {
        return null;
    }

    @Override
    public Builders.Any.F setJsonArrayBody(JsonArray jsonArray) {
        return null;
    }

    @Override
    public Builders.Any.F setStringBody(String string) {
        return null;
    }

    @Override
    public Builders.Any.F setDocumentBody(Document document) {
        return null;
    }

    @Override
    public Builders.Any.F setFileBody(File file) {
        return null;
    }

    @Override
    public Builders.Any.F setByteArrayBody(byte[] bytes) {
        return null;
    }

    @Override
    public Builders.Any.F setStreamBody(InputStream inputStream) {
        return null;
    }

    @Override
    public Builders.Any.F setStreamBody(InputStream inputStream, int length) {
        return null;
    }

    @Override
    public Builders.Any.M setMultipartParameter(String name, String value) {
        return null;
    }

    @Override
    public Builders.Any.M setMultipartParameters(Map<String, List<String>> params) {
        return null;
    }

    @Override
    public Builders.Any.M setMultipartFile(String name, File file) {
        return null;
    }

    @Override
    public Builders.Any.M setMultipartFile(String name, String contentType, File file) {
        return null;
    }

    @Override
    public Builders.Any.M addMultipartParts(Iterable<Part> parameters) {
        return null;
    }

    @Override
    public Builders.Any.M addMultipartParts(Part... parameters) {
        return null;
    }

    @Override
    public Builders.Any.M setMultipartContentType(String contentType) {
        return null;
    }

    @Override
    public Builders.Any.U setBodyParameter(String name, String value) {
        return null;
    }

    @Override
    public Builders.Any.U setBodyParameters(Map<String, List<String>> params) {
        return null;
    }
}
