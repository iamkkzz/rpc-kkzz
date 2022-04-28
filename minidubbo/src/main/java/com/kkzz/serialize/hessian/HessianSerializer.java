package com.kkzz.serialize.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.kkzz.exception.SerializeException;
import com.kkzz.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
@Slf4j
public class HessianSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        HessianOutput hessianOutput = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("序列化时有错误发生:", e);
            throw new SerializeException("序列化时有错误发生");
        } finally {
            if (hessianOutput != null) {
                try {
                    hessianOutput.close();
                } catch (IOException e) {
                    log.error("关闭流时有错误发生:", e);
                }
            }
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        HessianInput hessianInput = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            hessianInput = new HessianInput(byteArrayInputStream);
            return clazz.cast(hessianInput.readObject());
        } catch (IOException e) {
            log.error("序列化时有错误发生:", e);
            throw new SerializeException("序列化时有错误发生");
        } finally {
            if (hessianInput != null) hessianInput.close();
        }
    }

//    @Override
//    public Object deserialize(byte[] bytes, Class<?> clazz) {
//        HessianInput hessianInput = null;
//        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
//            hessianInput = new HessianInput(byteArrayInputStream);
//            return hessianInput.readObject();
//        } catch (IOException e) {
//            logger.error("序列化时有错误发生:", e);
//            throw new SerializeException("序列化时有错误发生");
//        } finally {
//            if (hessianInput != null) hessianInput.close();
//        }
//    }
}
