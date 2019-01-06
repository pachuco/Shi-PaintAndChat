package paintchat_frame;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;

public class DataBeanInfo extends SimpleBeanInfo {
    // $FF: synthetic field
    static Class class$0;
    // $FF: synthetic field
    static Class class$1;

    public static Method findMethod(Class var0, String var1, int var2) {
        try {
            Method[] var3 = var0.getMethods();

            for (int var4 = 0; var4 < var3.length; ++var4) {
                Method var5 = var3[var4];
                if (var5.getParameterTypes().length == var2 && var5.getName().equals(var1)) {
                    return var5;
                }
            }

            return null;
        } catch (Throwable var6) {
            return null;
        }
    }

    public BeanInfo[] getAdditionalBeanInfo() {
        BeanInfo var2 = null;

        Class var1;
        try {
            var1 = this.getBeanDescriptor().getBeanClass().getSuperclass();
        } catch (Throwable var5) {
            return null;
        }

        try {
            var2 = Introspector.getBeanInfo(var1);
        } catch (IntrospectionException var4) {
        }

        if (var2 != null) {
            BeanInfo[] var3 = new BeanInfo[]{var2};
            return var3;
        } else {
            return null;
        }
    }

    public static Class getBeanClass() {
        Class var10000 = class$0;
        if (var10000 == null) {
            try {
                var10000 = Class.forName("paintchat_frame.Data");
            } catch (ClassNotFoundException var0) {
                throw new NoClassDefFoundError(var0.getMessage());
            }

            class$0 = var10000;
        }

        return var10000;
    }

    public static String getBeanClassName() {
        return "paintchat_frame.Data";
    }

    //WARN: suspicious decompile
    /*public BeanDescriptor getBeanDescriptor() {
        //Fernflower decompile
        BeanDescriptor var1 = null;

        try {
            BeanDescriptor var10000 = new BeanDescriptor; //what?
            Class var10002 = class$0;
            if (var10002 == null) {
                try {
                    var10002 = Class.forName("paintchat_frame.Data");
                } catch (ClassNotFoundException var2) {
                    throw new NoClassDefFoundError(var2.getMessage());
                }

                class$0 = var10002;
            }

            //var10000.<init>(var10002); //what?
            var1 = var10000;
            var1.setDisplayName("paintchat_frame.Data");
            var1.setShortDescription("paintchat_frame.Data");
        } catch (Throwable var3) {
        }

        return var1;
    }*/

    public BeanDescriptor getBeanDescriptor() {
        //Procyon 0.5.30 decompile
        BeanDescriptor beanDescriptor = null;
        try {
            Class class$0;
            if ((class$0 = DataBeanInfo.class$0) == null) {
                try {
                    class$0 = (DataBeanInfo.class$0 = Class.forName("paintchat_frame.Data"));
                } catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            beanDescriptor = new BeanDescriptor(class$0);
            beanDescriptor.setDisplayName("paintchat_frame.Data");
            beanDescriptor.setShortDescription("paintchat_frame.Data");
        } catch (Throwable t) {
        }
        return beanDescriptor;
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        try {
            EventSetDescriptor[] var1 = new EventSetDescriptor[0];
            return var1;
        } catch (Throwable var2) {
            this.handleException(var2);
            return null;
        }
    }

    public Image getIcon(int var1) {
        Image var2 = null;
        if (var1 == 1) {
            var2 = this.loadImage("/cnf/icon.gif");
        }

        if (var1 == 3) {
            var2 = this.loadImage("/cnf/icon.gif");
        }

        return var2;
    }

    public MethodDescriptor[] getMethodDescriptors() {
        try {
            MethodDescriptor[] var1 = new MethodDescriptor[0];
            return var1;
        } catch (Throwable var2) {
            this.handleException(var2);
            return null;
        }
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor[] var1 = new PropertyDescriptor[]{this.isNativeWindowsPropertyDescriptor()};
            return var1;
        } catch (Throwable var2) {
            this.handleException(var2);
            return null;
        }
    }

    private void handleException(Throwable var1) {
    }

    public PropertyDescriptor isNativeWindowsPropertyDescriptor() {
        PropertyDescriptor var1 = null;

        try {
            try {
                Method var2 = null;

                Class[] var3;
                try {
                    var3 = new Class[0];
                    var2 = getBeanClass().getMethod("getIsNativeWindows", var3);
                } catch (Throwable var6) {
                    this.handleException(var6);
                    var2 = findMethod(getBeanClass(), "getIsNativeWindows", 0);
                }

                var3 = null;

                Method var9;
                try {
                    Class[] var4 = new Class[]{Boolean.TYPE};
                    var9 = getBeanClass().getMethod("setIsNativeWindows", var4);
                } catch (Throwable var5) {
                    this.handleException(var5);
                    var9 = findMethod(getBeanClass(), "setIsNativeWindows", 1);
                }

                var1 = new PropertyDescriptor("isNativeWindows", var2, var9);
            } catch (Throwable var7) {
                this.handleException(var7);
                var1 = new PropertyDescriptor("isNativeWindows", getBeanClass());
            }
        } catch (Throwable var8) {
            this.handleException(var8);
        }

        return var1;
    }
}
