/*------------------------------------------------------------------------*
 * Copyright 2019, aicas GmbH; all rights reserved.
 * This header, including copyright notice, may not be altered or removed.
 *------------------------------------------------------------------------*/
package com.aicas;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Bundle activator that starts a ModbusMasterThread worker thread.
 */
public class IoExampleActivator implements BundleActivator
{
  private Thread workerThread_;

  @Override
  public void start(BundleContext context)
    throws Exception
  {
    // Create and start the worker thread.
    System.out.println("starting the worker thread");
    workerThread_ = new IoExampleThread();
    workerThread_.start();
  }

  @Override
  public void stop(BundleContext context)
    throws Exception
  {
    // Ask the worker thread to terminate.
    System.out.println("Stopping the worker thread");
    workerThread_.interrupt();
  }
}
