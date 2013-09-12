package de.micralon.engine.net;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Network {
	@SuppressWarnings("rawtypes")
    protected static void registerLibGDXClasses(Kryo kryo) {
        kryo.register(Array.class, new Serializer<Array>() {
            {
                    setAcceptsNull(true);
            }

            private Class genericType;

            public void setGenerics (Kryo kryo, Class[] generics) {
                    if (kryo.isFinal(generics[0])) genericType = generics[0];
            }

            public void write (Kryo kryo, Output output, Array array) {
                    int length = array.size;
                    output.writeInt(length, true);
                    if (length == 0) return;
                    if (genericType != null) {
                            Serializer serializer = kryo.getSerializer(genericType);
                            genericType = null;
                            for (Object element : array)
                                    kryo.writeObjectOrNull(output, element, serializer);
                    } else {
                            for (Object element : array)
                                    kryo.writeClassAndObject(output, element);
                    }
            }

            @SuppressWarnings("unchecked")
			public Array read (Kryo kryo, Input input, Class<Array> type) {
                    Array array = new Array();
                    kryo.reference(array);
                    int length = input.readInt(true);
                    array.ensureCapacity(length);
                    if (genericType != null) {
                            Class elementClass = genericType;
                            Serializer serializer = kryo.getSerializer(genericType);
                            genericType = null;
                            for (int i = 0; i < length; i++)
                                    array.add(kryo.readObjectOrNull(input, elementClass, serializer));
                    } else {
                            for (int i = 0; i < length; i++)
                                    array.add(kryo.readClassAndObject(input));
                    }
                    return array;
            }
	    });
        
        kryo.register(IntArray.class, new Serializer<IntArray>() {
            {
                    setAcceptsNull(true);
            }

            public void write (Kryo kryo, Output output, IntArray array) {
                    int length = array.size;
                    output.writeInt(length, true);
                    if (length == 0) return;
                    for (int i = 0, n = array.size; i < n; i++)
                            output.writeInt(array.get(i), true);
            }

            public IntArray read (Kryo kryo, Input input, Class<IntArray> type) {
                    IntArray array = new IntArray();
                    kryo.reference(array);
                    int length = input.readInt(true);
                    array.ensureCapacity(length);
                    for (int i = 0; i < length; i++)
                            array.add(input.readInt(true));
                    return array;
            }
        });

	    kryo.register(FloatArray.class, new Serializer<FloatArray>() {
	            {
	                    setAcceptsNull(true);
	            }
	
	            public void write (Kryo kryo, Output output, FloatArray array) {
	                    int length = array.size;
	                    output.writeInt(length, true);
	                    if (length == 0) return;
	                    for (int i = 0, n = array.size; i < n; i++)
	                            output.writeFloat(array.get(i));
	            }
	
	            public FloatArray read (Kryo kryo, Input input, Class<FloatArray> type) {
	                    FloatArray array = new FloatArray();
	                    kryo.reference(array);
	                    int length = input.readInt(true);
	                    array.ensureCapacity(length);
	                    for (int i = 0; i < length; i++)
	                            array.add(input.readFloat());
	                    return array;
	            }
	    });

	
	    kryo.register(Color.class, new Serializer<Color>() {
	            public Color read (Kryo kryo, Input input, Class<Color> type) {
	                    Color color = new Color();
	                    Color.rgba8888ToColor(color, input.readInt());
	                    return color;
	            }
	
	            public void write (Kryo kryo, Output output, Color color) {
	                    output.writeInt(Color.rgba8888(color));
	            }
	    });
	}
}
