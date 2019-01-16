package com.aicas;

import java.io.IOException;

import jdk.dio.ClosedDeviceException;
import jdk.dio.DeviceManager;
import jdk.dio.DeviceNotFoundException;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedByteOrderException;
import jdk.dio.UnsupportedDeviceTypeException;
import jdk.dio.spibus.SPIDevice;

public class SpiDriver
{
  private SPIDevice spiDevice_ = null;

  SpiDriver()
  {
    try
      {
        spiDevice_ =
          (SPIDevice) DeviceManager.open("SPI0.0", SPIDevice.class, null);
      }
    catch (UnsupportedDeviceTypeException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    catch (DeviceNotFoundException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    catch (UnavailableDeviceException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  }

  public int read(java.nio.ByteBuffer dst)
  {
    int returnvalue = -1;
    try
      {
        returnvalue = spiDevice_.read(dst);
      }
    catch (UnavailableDeviceException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    catch (UnsupportedByteOrderException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    catch (ClosedDeviceException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    return returnvalue;
  }

  public boolean isOpen()
  {
    return spiDevice_.isOpen();
  }
}
