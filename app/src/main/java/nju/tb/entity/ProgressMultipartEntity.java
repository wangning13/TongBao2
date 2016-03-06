package nju.tb.entity;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

//反馈上传进度的entity
public class ProgressMultipartEntity extends MultipartEntity {
    private ProgressListener progressListener;

    public ProgressMultipartEntity(final ProgressListener listener) {
        super();
        this.progressListener = listener;
    }

    public ProgressMultipartEntity(final HttpMultipartMode mode,
                                   final ProgressListener listener) {
        super(mode);
        this.progressListener = listener;
    }

    public ProgressMultipartEntity(HttpMultipartMode mode, final String boundary,
                                   final Charset charset, final ProgressListener listener) {
        super(mode, boundary, charset);
        this.progressListener = listener;
    }

    @Override
    public void writeTo(OutputStream outputStream) {
        try {
            super.writeTo(new CountingOutputStream(outputStream, this.progressListener));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class CountingOutputStream extends FilterOutputStream {
        private final ProgressListener listener;
        private long count;

        public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
            super(out);
            this.listener = listener;
            count = 0;
        }

        @Override
        public void write(byte[] b, int off, int len) {
            try {
                out.write(b, off, len);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.count += len;
            this.listener.transferred(count);
        }

        @Override
        public void write(int b) {
            try {
                out.write(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.count++;
            this.listener.transferred(this.count);
        }
    }

    public static interface ProgressListener {
        void transferred(long num);
    }
}
