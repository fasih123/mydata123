/*------------------------------------------------------------------------*
 * Copyright 2019, aicas GmbH; all rights reserved.
 * This header, including copyright notice, may not be altered or removed.
 *------------------------------------------------------------------------*/
package com.aicas;

import java.io.IOException;
import java.nio.ByteBuffer;

import jdk.dio.DeviceConfig;
import jdk.dio.DeviceManager;
import jdk.dio.DeviceNotFoundException;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedDeviceTypeException;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.gpio.GPIOPinConfig;
import jdk.dio.spibus.SPIDevice;

/**
 * Worker thread that sends SPI data.
 */
class IoExampleThread extends Thread
{

  private SPIDevice spiDevice_ = null;

  public void blinkLEDByGPIONumber(int pinNumber)
  {
    System.out.println("Blinking LED on GPIO pin number " + pinNumber);
    GPIOPin pin = null;
    try
      {
        GPIOPinConfig pinConfig = new GPIOPinConfig(DeviceConfig.DEFAULT,
                                                    pinNumber,
                                                    GPIOPinConfig.DIR_OUTPUT_ONLY,
                                                    GPIOPinConfig.MODE_OUTPUT_PUSH_PULL,
                                                    GPIOPinConfig.TRIGGER_NONE,
                                                    false);
        pin = (GPIOPin) DeviceManager.open(GPIOPin.class, pinConfig);
        boolean pinOn = false;
        for (int i = 0; i < 20; i++)
          {
            Thread.sleep(2500);
            pinOn = !pinOn;
            pin.setValue(pinOn);
          }
      }
    catch (IOException ioe)
      {
        throw new LEDExampleException("IOException while opening device. Make sure you have the appropriate operating system permission to access GPIO devices.",
                                      ioe);
      }
    catch (InterruptedException ie)
      {
        // ignore
      }
    finally
      {
        try
          {
            System.out.println("Closing GPIO pin number " + pinNumber);
            if (pin != null)
              {
                pin.close();
              }
          }
        catch (Exception e)
          {
            throw new LEDExampleException("Received exception while trying to close device.",
                                          e);
          }
      }
  }

  public void blinkLEDByDeviceId(int deviceId)
  {
    System.out.println("Blinking LED on device ID " + deviceId);
    GPIOPin pin = null;
    try
      {
        pin = (GPIOPin) DeviceManager.open(deviceId);
        boolean pinOn = false;
        for (int i = 0; i < 20; i++)
          {
            Thread.sleep(2500);
            pinOn = !pinOn;
            pin.setValue(pinOn);
          }
      }
    catch (IOException ioe)
      {
        throw new LEDExampleException("IOException while opening device. Make sure you have the appropriate operating system permission to access GPIO devices.",
                                      ioe);
      }
    catch (InterruptedException ie)
      {
        // ignore
      }
    finally
      {
        try
          {
            System.out.println("Closing device ID " + deviceId);
            if (pin != null)
              {
                pin.close();
              }
          }
        catch (Exception e)
          {
            throw new LEDExampleException("Received exception while trying to close device.",
                                          e);
          }
      }
  }

  IoExampleThread()
  {
  }

  @Override
  public void run()
  {
    System.out.println("Reading SPI");
    ByteBuffer out = ByteBuffer.allocate(4);
    ByteBuffer in = ByteBuffer.allocate(6);
    out.put((byte) 0x80);
    out.put((byte) 0x00);
    out.put((byte) 0x00);
    out.flip();

    try
      {
        //spiDevice_ =
        //  (SPIDevice) DeviceManager.open("SPI0.0", SPIDevice.class, null);
        spiDevice_ =
          (SPIDevice) DeviceManager.open(300);
        
        spiDevice_.writeAndRead(out, in);
        System.out.println("Response1: " + in.get(0));
        System.out.println("Response1: " + in.get(1));
        System.out.println("Response2: " + in.get(2));
//        System.out.println("Response3: " + in.get(3));

        out.clear();
        out.put((byte) 0x7D);
        out.put((byte) 0x04);
        out.put((byte) 0);
        out.put((byte) 0);
        out.flip();
        in.clear();
        spiDevice_.writeAndRead(out, in);
        System.out.println("Response1: " + in.get(0));
        System.out.println("Response1: " + in.get(1));
        System.out.println("Response2: " + in.get(2));
        
        sleep(2000);

        out.clear();
        out.put((byte) 0xFD);
        out.put((byte) 0);
        out.put((byte) 0);
        out.flip();
        in.clear();
        spiDevice_.writeAndRead(out, in);
        System.out.println("Response1: " + in.get(0));
        System.out.println("Response1: " + in.get(1));
        System.out.println("Response2: " + in.get(2));
        

//        out.clear();
//        out.put((byte) 0x40);
//        out.put((byte) 0x17);
//        out.put((byte) 0);
//        out.put((byte) 0);
//        out.flip();
//        in.clear();
//        spiDevice_.writeAndRead(out, in);
//        System.out.println("Response1: " + in.get(0));
//        System.out.println("Response1: " + in.get(1));
//        System.out.println("Response2: " + in.get(2));
//
//        out.clear();
//        out.put((byte) 0x7C);
//        out.put((byte) 0x03);
//        out.put((byte) 0);
//        out.put((byte) 0);
//        out.flip();
//        in.clear();
//        spiDevice_.writeAndRead(out, in);
//        System.out.println("Response1: " + in.get(0));
//        System.out.println("Response1: " + in.get(1));
//        System.out.println("Response2: " + in.get(2));
//
//        in.clear();
//        spiDevice_.read(in);
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
    catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    finally
      {
        try
          {
            spiDevice_.close();
          }
        catch (IOException e)
          {
            e.printStackTrace();
          }
      }
    System.out.println("Start Blinking");
    //blinkLEDByGPIONumber(23);
    blinkLEDByDeviceId(6);


  }

  class LEDExampleException extends RuntimeException
  {
    private static final long serialVersionUID = -374914030760964070L;

    public LEDExampleException(String msg, Throwable t)
    {
      super(msg, t);
    }
  }

}
