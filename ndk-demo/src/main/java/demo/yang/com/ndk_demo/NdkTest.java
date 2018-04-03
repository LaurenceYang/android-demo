package demo.yang.com.ndk_demo;

public class NdkTest {
    static {
        System.loadLibrary("ndk_demo_jni");
        native_init();
    }

    private static native final void native_init();
    public native void start() throws IllegalStateException;
}