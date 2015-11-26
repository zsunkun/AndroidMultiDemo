/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\WorkSpace\\AndroidTest\\src\\aidl\\calculate\\ICalculateService.aidl
 */
package aidl.calculate;
public interface ICalculateService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements aidl.calculate.ICalculateService
{
private static final java.lang.String DESCRIPTOR = "aidl.calculate.ICalculateService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an aidl.calculate.ICalculateService interface,
 * generating a proxy if needed.
 */
public static aidl.calculate.ICalculateService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof aidl.calculate.ICalculateService))) {
return ((aidl.calculate.ICalculateService)iin);
}
return new aidl.calculate.ICalculateService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_calcul:
{
data.enforceInterface(DESCRIPTOR);
double _arg0;
_arg0 = data.readDouble();
double _arg1;
_arg1 = data.readDouble();
double _result = this.calcul(_arg0, _arg1);
reply.writeNoException();
reply.writeDouble(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements aidl.calculate.ICalculateService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public double calcul(double one, double two) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
double _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeDouble(one);
_data.writeDouble(two);
mRemote.transact(Stub.TRANSACTION_calcul, _data, _reply, 0);
_reply.readException();
_result = _reply.readDouble();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_calcul = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public double calcul(double one, double two) throws android.os.RemoteException;
}
