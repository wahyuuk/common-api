package co.id.kuncoro.commonapi.helper;

import co.id.kuncoro.commonapi.util.MapperUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class HttpResponseWrapper extends HttpServletResponseWrapper {

  private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
  private final PrintWriter printWriter;
  private final OutputStream outputStream;

  private final MapperUtils mapperUtils;

  public HttpResponseWrapper(HttpServletResponse response, MapperUtils mapperUtils) throws IOException {
    super(response);
    this.printWriter = new PrintWriter(this.byteArrayOutputStream, true);
    this.outputStream = response.getOutputStream();
    this.mapperUtils = mapperUtils;
  }

  @Override
  public final ServletOutputStream getOutputStream() {
    return new ServletOutputStreamWrapper(this.byteArrayOutputStream);
  }

  @Override
  public final PrintWriter getWriter() {
    return this.printWriter;
  }


  public final byte[] getBody() {
    return byteArrayOutputStream.toByteArray();
  }

  public final String getBodyAsString() {
    return mapperUtils.writeValueAsString(getBody());
  }

  public final void setBody(final byte[] data) throws IOException {
    this.reset();
    this.byteArrayOutputStream.write(data);
  }

  public final void setBody(final String data) throws IOException {
    byte[] bdata;
    if (null == data) {
      bdata = new byte[0];
    } else {
      bdata = data.getBytes();
    }

    this.setBody(bdata);
  }

  public final synchronized void flush() throws IOException {
    this.outputStream.write(this.byteArrayOutputStream.toByteArray());
    this.outputStream.close();
  }

  @Override
  public final void reset() {
    this.byteArrayOutputStream.reset();
  }

  @Override
  public final String toString() {
    String ret;
    ret = new String(this.getBody(), StandardCharsets.UTF_8);
    return ret;
  }

  public static class ServletOutputStreamWrapper extends ServletOutputStream {

    private final OutputStream outputStream;

    public ServletOutputStreamWrapper(OutputStream outputStream) {
      this.outputStream = outputStream;
    }


    @Override
    public boolean isReady() {
      return true;
    }

    @Override
    public void write(int b) throws IOException {
      this.outputStream.write(b);
    }


    @Override
    public void setWriteListener(WriteListener listener) {
    }

  }

}
