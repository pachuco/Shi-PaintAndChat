/*
 * Copyright 1995-2003 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package jaba.applet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.applet.AudioClip;

import com.sun.media.sound.JavaSoundAudioClip;


/**
 * Applet audio clip;
 *
 * @author Arthur van Hoff, Kara Kytle
 */

public class AppletAudioClip implements AudioClip {

    // url that this AudioClip is based on
    private URL url = null;

    // the audio clip implementation
    private AudioClip audioClip = null;

    boolean DEBUG = false /*true*/;

    /**
     * Constructs an AppletAudioClip from an URL.
     */
    public AppletAudioClip(URL url) {

        // store the url
        this.url = url;

        try {
            // create a stream from the url, and use it
            // in the clip.
            InputStream in = url.openStream();
            createAppletAudioClip(in);

        } catch (IOException ex) {
                /* just quell it */
            if (DEBUG) {
                System.err.println("IOException creating AppletAudioClip" + ex);
            }
        }
    }

    /**
     * Constructs an AppletAudioClip from a URLConnection.
     */
    public AppletAudioClip(URLConnection uc) {

        try {
            // create a stream from the url, and use it
            // in the clip.
            createAppletAudioClip(uc.getInputStream());

        } catch (IOException ex) {
                /* just quell it */
            if (DEBUG) {
                System.err.println("IOException creating AppletAudioClip" + ex);
            }
        }
    }

    /**
     * Constructs an AppletAudioClip from a URLConnection.
     */
    public AppletAudioClip(InputStream in) {

        try {
            // use stream directly in the clip.
            createAppletAudioClip(in);

        } catch (IOException ex) {
                /* just quell it */
            if (DEBUG) {
                System.err.println("IOException creating AppletAudioClip" + ex);
            }
        }
    }

    /**
     * For constructing directly from Jar entries, or any other
     * raw Audio data. Note that the data provided must include the format
     * header.
     */
    public AppletAudioClip(byte[] data) {

        try {

            // construct a stream from the byte array
            InputStream in = new ByteArrayInputStream(data);

            createAppletAudioClip(in);

        } catch (IOException ex) {
                /* just quell it */
            if (DEBUG) {
                System.err.println("IOException creating AppletAudioClip " + ex);
            }
        }
    }


    /*
     * Does the real work of creating an AppletAudioClip from an InputStream.
     * This function is used by all constructors.
     */
    void createAppletAudioClip(InputStream in) throws IOException {

        try {
            audioClip = new JavaSoundAudioClip(in);
        } catch (Exception e3) {
            // no matter what happened, we throw an IOException to avoid changing the interfaces....
            throw new IOException("Failed to construct the AudioClip: " + e3);
        }
    }


    public synchronized void play() {

        if (audioClip != null)
            audioClip.play();
    }


    public synchronized void loop() {

        if (audioClip != null)
            audioClip.loop();
    }

    public synchronized void stop() {

        if (audioClip != null)
            audioClip.stop();
    }
}
