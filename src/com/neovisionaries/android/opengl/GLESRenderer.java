/*
 * (C) 2012 Neo Visionaries Inc. All Rights Reserved.
 */
package com.neovisionaries.android.opengl;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;


/**
 * Renderer for OpenGL ES.
 *
 * <p>
 * Instances of this class should be set to {@link GLESSurfaceView}
 * so that their callback methods such as {@link
 * #onKeyDown(GLESSurfaceView, int, KeyEvent) onKeyDown()} can be
 * called.
 * </p>
 *
 * @author Takahiko Kawasaki
 */
public abstract class GLESRenderer implements GLSurfaceView.Renderer
{
    /**
     * This method calls {@link #onDrawFrame(GLES)}.
     */
    @Override
    public final void onDrawFrame(GL10 gl10)
    {
        onDrawFrame(GLESFactory.getInstance());
    }


    /**
     * This method calls {@link #onSurfaceChanged(GLES, int width, int height)}.
     */
    @Override
    public final void onSurfaceChanged(GL10 gl10, int width, int height)
    {
        onSurfaceChanged(GLESFactory.getInstance(), width, height);
    }


    /**
     * This method calls {@link #onSurfaceCreated(GLES, EGLConfig)}.
     */
    @Override
    public final void onSurfaceCreated(GL10 gl10, EGLConfig config)
    {
        onSurfaceCreated(GLESFactory.getInstance(), config);
    }


    public abstract void onDrawFrame(GLES gles);


    public abstract void onSurfaceChanged(GLES gles, int width, int height);


    public abstract void onSurfaceCreated(GLES gles, EGLConfig config);


    /**
     * Callback method for KeyDown event.
     *
     * <p>
     * This method is called when the {@link GLESSurfaceView} instance
     * which this renderer is set to receives a KeyDown event.
     * </p>
     *
     * @param view
     *         The GLESSurfaceView instance which this renderer is set to.
     *
     * @param keyCode
     * @param event
     *
     * @return
     *         True if the implementation of this method has consumed the event.
     *
     * @see GLESSurfaceView#onKeyDown(int, KeyEvent)
     */
    public boolean onKeyDown(GLESSurfaceView view, int keyCode, KeyEvent event)
    {
        return false;
    }


    /**
     * Callback method for KeyLongPress event.
     *
     * <p>
     * This method is called when the {@link GLESSurfaceView} instance
     * which this renderer is set to receives a KeyLongPress event.
     * </p>
     *
     * @param view
     *         The GLESSurfaceView instance which this renderer is set to.
     *
     * @param keyCode
     * @param event
     *
     * @return
     *         True if the implementation of this method has consumed the event.
     *
     * @see GLESSurfaceView#onKeyLongPress(int, KeyEvent)
     */
    public boolean onKeyLongPress(GLESSurfaceView view, int keyCode, KeyEvent event)
    {
        return false;
    }


    /**
     * Callback method for KeyUp event.
     *
     * <p>
     * This method is called when the {@link GLESSurfaceView} instance
     * which this renderer is set to receives a KeyUp event.
     * </p>
     *
     * @param view
     *         The GLESSurfaceView instance which this renderer is set to.
     *
     * @param keyCode
     * @param event
     *
     * @return
     *         True if the implementation of this method has consumed the event.
     *
     * @see GLESSurfaceView#onKeyUp(int, KeyEvent)
     */
    public boolean onKeyUp(GLESSurfaceView view, int keyCode, KeyEvent event)
    {
        return false;
    }


    /**
     * Callback method for Touch event.
     *
     * <p>
     * This method is called when the {@link GLESSurfaceView} instance
     * which this renderer is set to receives a Touch event.
     * </p>
     *
     * @param view
     *         The GLESSurfaceView instance which this renderer is set to.
     *
     * @param event
     *
     * @return
     *         True if the implementation of this method has consumed the event.
     *
     * @see GLESSurfaceView#onTouchEvent(MotionEvent)
     */
    public boolean onTouchEvent(GLESSurfaceView view, MotionEvent event)
    {
        return false;
    }
}