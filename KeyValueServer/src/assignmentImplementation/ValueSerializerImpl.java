package assignmentImplementation;

import java.io.IOException;
import java.util.List;

import keyValueBaseInterfaces.ValueSerializer;

public class ValueSerializerImpl implements ValueSerializer<ValueListImpl> {

    @Override
    public ValueListImpl fromByteArray(byte[] array) throws IOException {
        if (array.length % 4 != 0)
            return null;
        ValueListImpl vl = new ValueListImpl();
        for (int i = 0; i < array.length; i+=4) {
            int j = (array[i+3] & 0x000000FF);
            j += (array[i+2] & 0x000000FF) << 8;
            j += (array[i+1] & 0x000000FF) << 16;
            j += (array[i] & 0x000000FF) << 24;
            ValueImpl v = new ValueImpl();
            v.setValue(j);
            vl.add(v);
        }
        return vl;
    }

    @Override
    public byte[] toByteArray(ValueListImpl v) throws IOException {
        List<ValueImpl> l = v.toList();
        byte[] out = new byte[l.size() * 4];
        int idx = 0;
        for (ValueImpl vi : l) {
            Integer i2 = vi.getValue();
            int i = i2;
            out[idx] = (byte)(i >>> 24);
            out[idx+1] = (byte)(i >>> 16);
            out[idx+2] = (byte)(i >>> 8);
            out[idx+3] = (byte)i;
            idx += 4;
        }
        return out;
    }

}
