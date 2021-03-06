/*******************************************************************************
 * Copyright (c) 2016 Chen Chao(cnfree2000@hotmail.com).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Chen Chao  - initial API and implementation
 *******************************************************************************/

package org.sf.feeling.decompiler.preferences;

import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ShareBrowser
{

	private Browser browser;
	private Shell shell;

	public ShareBrowser( final Composite parent, int style )
	{
		browser = new Browser( parent, style );
		browser.setUrl( "http://www.chenchao.tk/decompiler/share.html?token=" //$NON-NLS-1$
				+ System.currentTimeMillis( ) );
		browser.addProgressListener( new ProgressListener( ) {

			public void completed( ProgressEvent event )
			{
				Color bgColor = parent.getBackground( );
				String rgb = "rgb(" //$NON-NLS-1$
						+ bgColor.getRed( )
						+ "," //$NON-NLS-1$
						+ bgColor.getGreen( )
						+ "," //$NON-NLS-1$
						+ bgColor.getBlue( )
						+ ")"; //$NON-NLS-1$
				browser.execute( "document.body.style.backgroundColor = '" //$NON-NLS-1$
						+ rgb
						+ "';" ); //$NON-NLS-1$

				if ( Boolean.TRUE
						.equals( browser.evaluate( "return isLoaded();" ) ) ) //$NON-NLS-1$
				{
					browser.execute( "adjustLayout();" ); //$NON-NLS-1$

					GridData gd = (GridData) browser.getLayoutData( );
					gd.grabExcessVerticalSpace = true;
					gd.grabExcessVerticalSpace = true;
					gd.horizontalAlignment = SWT.FILL;
					gd.verticalAlignment = SWT.FILL;

					browser.setLayoutData( gd );
					browser.getParent( ).layout( );
					browser.getParent( ).getParent( ).layout( );
					browser.setVisible( true );
				}
			}

			public void changed( ProgressEvent event )
			{

			}
		} );

		browser.addOpenWindowListener( new OpenWindowListener( ) {

			public void open( WindowEvent e )
			{
				if ( shell != null && !shell.isDisposed( ) )
				{
					shell.dispose( );
				}

				shell = new Shell( Display.getDefault( ) );
				shell.setVisible( false );

				final Browser linkBrowser = new Browser( shell, SWT.NONE );
				linkBrowser.setVisible( true );
				e.browser = linkBrowser;
				linkBrowser.addLocationListener( new LocationListener( ) {

					public void changing( LocationEvent event )
					{
						try
						{
							PlatformUI.getWorkbench( )
									.getBrowserSupport( )
									.getExternalBrowser( )
									.openURL( new URL( event.location ) );
							Display.getDefault( ).asyncExec( new Runnable( ) {

								public void run( )
								{
									if ( shell != null && !shell.isDisposed( ) )
									{
										shell.dispose( );
									}
								}
							} );
						}
						catch ( Throwable e )
						{
							e.printStackTrace( );
						}
					}

					public void changed( LocationEvent event )
					{
						try
						{
							PlatformUI.getWorkbench( )
									.getBrowserSupport( )
									.getExternalBrowser( )
									.openURL( new URL( event.location ) );
							Display.getDefault( ).asyncExec( new Runnable( ) {

								public void run( )
								{
									if ( shell != null && !shell.isDisposed( ) )
									{
										shell.dispose( );
									}
								}
							} );
						}
						catch ( Throwable e )
						{
							e.printStackTrace( );
						}
					}
				} );
			}
		} );

		browser.addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				if ( shell != null && !shell.isDisposed( ) )
				{
					shell.dispose( );
				}
			}
		} );
	}

	public Browser getBrowser( )
	{
		return browser;
	}
}
