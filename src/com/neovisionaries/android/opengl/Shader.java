/*
 * Copyright (C) 2012 Neo Visionaries Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neovisionaries.android.opengl;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import static com.neovisionaries.android.opengl.ShaderState.COMPILED;
import static com.neovisionaries.android.opengl.ShaderState.CREATED;
import static com.neovisionaries.android.opengl.ShaderState.DELETED;
import static com.neovisionaries.android.opengl.ShaderState.SOURCE_SET;


/**
 * OpenGL ES shader.
 *
 * <pre style="background: lightgray;">
 * <span style="color: darkgreen;">// <b>E X A M P L E   1</b></span>
 *
 * <span style="color: darkgreen;">// Create a shader.</span>
 * {@link Shader} shader = new {@link VertexShader}();
 * <span style="color: darkgreen;">//Shader shader = new {@link FragmentShader}();</span>
 *
 * <span style="color: darkgreen;">// Set a shader source code.</span> 
 * String shaderSource = <span style="color: brown;">"..."</span>;
 * shader.{@link #setSource(String) setSource}(shaderSource);
 *
 * <span style="color: darkgreen;">// Compile the shader source code.</span>
 * shader.{@link #compile() compile}();
 * </pre>
 *
 * <pre style="background: lightgray;">
 * <span style="color: darkgreen;">// <b>E X A M P L E   2</b></span>
 *
 * <span style="color: darkgreen;">// Just one line.</span>
 * {@link Shader} shader = new {@link VertexShader}(<span style="color: brown;">"..."</span>).{@link #compile() compile}();
 * </pre>
 *
 * @author Takahiko Kawasaki
 *
 * @see Program
 */
public abstract class Shader<TShader extends Shader<TShader>>
{
    /**
     * Shader ID. A return value from glCreateShader().
     */
    private final int id;


    /**
     * State of this shader.
     */
    private ShaderState state = CREATED;


    /**
     * Auto deletion when detached.
     */
    private boolean autoDeleted;


    /**
     * Programs that this shader is attached to.
     */
    private List<Program> programList = new LinkedList<Program>();


    /**
     * A constructor with a shader type. After this constructor
     * returns, the state of this instance is {@link ShaderState#CREATED}.
     *
     * @param type
     *         {@link ShaderType#VERTEX} or {@link ShaderType#FRAGMENT}.
     *
     * @throws IllegalArgumentException
     *         The given argument is null.
     *
     * @throws GLESException
     *         glCreateShader() failed.
     *
     * @see VertexShader#VertexShader()
     * @see FragmentShader#FragmentShader()
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCreateShader.xml">glCreateShader</a>
     */
    protected Shader(ShaderType type) throws GLESException
    {
        // Check the argument.
        if (type == null)
        {
            // The argument is invalid.
            throw new IllegalArgumentException("Shader type is null.");
        }

        // Create a shader of the given type.
        id = getGLES().glCreateShader(type.getType());

        // Check the result of glCreateShader().
        if (id == 0)
        {
            // Failed to create a shader.
            throw new GLESException("glCreateShader() failed.");
        }
    }


    /**
     * A constructor with a shader type and a shader source.
     * After this constructor returns, the state of this instance
     * is {@link ShaderState#SOURCE_SET}.
     *
     * @param type
     *         {@link ShaderType#VERTEX} or {@link ShaderType#FRAGMENT}.
     *
     * @param source
     *         A shader source code.
     *
     * @throws IllegalArgumentException
     *         Either or both of the arguments are null.
     *
     * @throws GLESException
     *         glCreateShader() failed.
     *
     * @see VertexShader#VertexShader(String)
     * @see FragmentShader#FragmentShader(String)
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCreateShader.xml">glCreateShader</a>
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glShaderSource.xml">glShaderSource</a>
     */
    protected Shader(ShaderType type, String source) throws GLESException
    {
        this(type);

        // Check the argument.
        if (source == null)
        {
            // The argument is invalid.
            throw new IllegalArgumentException("Shader source is null.");
        }

        // Set the given string as a shader source code.
        getGLES().glShaderSource(id, source);

        // A shader source was set.
        state = SOURCE_SET;
    }


    /**
     * A constructor with a shader type and a shader source file.
     * After this constructor returns, the state of this instance
     * is {@link ShaderState#SOURCE_SET}.
     *
     * @param type
     *         {@link ShaderType#VERTEX} or {@link ShaderType#FRAGMENT}.
     *
     * @param file
     *         A file whose content is a shader source code.
     *
     * @throws IllegalArgumentException
     *         Either or both of the arguments are null.
     *
     * @throws IOException
     *         Failed to read the content of the given file.
     *
     * @throws GLESException
     *         glCreateShader() failed.
     *
     * @see VertexShader#VertexShader(File)
     * @see FragmentShader#FragmentShader(File)
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCreateShader.xml">glCreateShader</a>
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glShaderSource.xml">glShaderSource</a>
     */
    protected Shader(ShaderType type, File file) throws IOException, GLESException
    {
        this(type, GLESHelper.toString(file));
    }


    /**
     * This constructor is an alias of {@link #Shader(ShaderType,
     * InputStream, boolean) Shader}(type, in, false).
     *
     * @see Shader#Shader(ShaderType, InputStream, boolean)
     * @see VertexShader#VertexShader(InputStream)
     * @see FragmentShader#FragmentShader(InputStream)
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCreateShader.xml">glCreateShader</a>
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glShaderSource.xml">glShaderSource</a>
     */
    protected Shader(ShaderType type, InputStream in) throws IOException, GLESException
    {
        this(type, in, false);
    }


    /**
     * A constructor with a shader type and an input stream from
     * which a shader source should be read. After this constructor
     * returns, the state of this instance is {@link
     * ShaderState#SOURCE_SET}.
     *
     * @param type
     *         {@link ShaderType#VERTEX} or {@link ShaderType#FRAGMENT}.
     *
     * @param in
     *         An input stream that feeds a shader source code.
     *
     * @param close
     *         If true is given, the input stream is closed before
     *         this method returns.
     *
     * @throws IllegalArgumentException
     *         'type' is null or 'in' is null.
     *
     * @throws IOException
     *         Failed to read the content of the input stream.
     *
     * @throws GLESException
     *         glCreateShader() failed.
     *
     * @see VertexShader#VertexShader(InputStream, boolean)
     * @see FragmentShader#FragmentShader(InputStream, boolean)
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCreateShader.xml">glCreateShader</a>
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glShaderSource.xml">glShaderSource</a>
     */
    protected Shader(ShaderType type, InputStream in, boolean close) throws IOException, GLESException
    {
        this(type, GLESHelper.toString(in, close));
    }


    /**
     * Get the shader ID which is a return value from glCreateShader().
     *
     * @return
     *         The shader ID assigned to this shader.
     *
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCreateShader.xml">glCreateShader</a>
     */
    public int getId()
    {
        return id;
    }


    /**
     * Get the state of this shader.
     *
     * @return
     *         The state of this shader.
     */
    public ShaderState getState()
    {
        return state;
    }


    /**
     * Delete this shader. If this shader has already been deleted,
     * nothing is executed. After this method returns, the state of
     * this instance is {@link ShaderState#DELETED}.
     *
     * <p>
     * Note that calling this method detaches this shader from all
     * the programs that this shader is currently attached to.
     * </p>
     *
     * <p>
     * If auto-deletion is enabled by {@link #setAutoDeleted(boolean)
     * setAutoDeleted}(true), this method is automatically called
     * when this instance is detached from the last program that
     * this instance is attached to.
     * </p>
     *
     * @return
     *         This Shader object.
     *
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glDeleteShader.xml">glDeleteShader</a>
     * @see #setAutoDeleted(boolean)
     */
    @SuppressWarnings("unchecked")
    public TShader delete()
    {
        // Check the state of this shader.
        if (state == DELETED)
        {
            // Already deleted.
            return (TShader)this;
        }

        // Delete this shader.
        getGLES().glDeleteShader(id);

        // This shader was deleted.
        state = DELETED;

        // For each program that this shader is attached to.
        for (Program program : programList)
        {
            // Notify the program that this shader was deleted.
            program.onShaderDeleted(this);
        }

        // Not keep track of programs any more.
        programList.clear();
        programList = null;

        return (TShader)this;
    }


    /**
     * Check if auto-deletion is enabled. The default value is false.
     *
     * @return
     *         True if auto-deletion is enabled. Otherwise, false.
     */
    public boolean isAutoDeleted()
    {
        return autoDeleted;
    }


    /**
     * Enable/disable auto-deletion.
     *
     * <p>
     * If 'true' is given to this method, auto-deletion is enabled,
     * meaning that {@link #delete()} is called automatically when
     * this instance is detached from the last program that this
     * instance was attached to.
     * </p>
     *
     * @param autoDeleted
     *         True to enable auto-deletion. False to disable it.
     *
     * @return
     *         This Shader object.
     */
    @SuppressWarnings("unchecked")
    public TShader setAutoDeleted(boolean autoDeleted)
    {
        this.autoDeleted = autoDeleted;

        return (TShader)this;
    }


    /**
     * Set a shader source code. If this method returns without
     * any execption, the state of this instance is {@link
     * ShaderState#SOURCE_SET}.
     *
     * @param source
     *         A shader source code.
     *
     * @return
     *         This Shader object.
     *
     * @throws IllegalArgumentException
     *         The given argument is null.
     *
     * @throws IllegalStateException
     *         This shader has already been deleted.
     *
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glShaderSource.xml">glShaderSource</a>
     */
    @SuppressWarnings("unchecked")
    public TShader setSource(String source)
    {
        // Check the argument.
        if (source == null)
        {
            // The argument is invalid.
            throw new IllegalArgumentException("Shader source is null.");
        }

        // Check the state of this shader.
        if (state == DELETED)
        {
            // Already deleted.
            throw new IllegalStateException("Shader has already been deleted.");
        }

        // Set the given string as a shader source code.
        getGLES().glShaderSource(id, source);

        // A shader source was set.
        state = SOURCE_SET;

        return (TShader)this;
    }


    /**
     * Set a shader source code. If this method returns without
     * any execption, the state of this instance is {@link
     * ShaderState#SOURCE_SET}.
     *
     * @param file
     *         A file that contains a shader source code.
     *
     * @return
     *         This Shader object.
     *
     * @throws IllegalArgumentException
     *         The given argument is null.
     *
     * @throws IllegalStateException
     *         This shader has already been deleted.
     *
     * @throws IOException
     *         Failed to read the given file.
     *
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glShaderSource.xml">glShaderSource</a>
     */
    public TShader setSource(File file) throws IOException
    {
        return setSource(GLESHelper.toString(file));
    }


    /**
     * Compile the source code given by {@link #setSource(String)}.
     * If the current state of this shader is {@link
     * ShaderState#COMPILED COMPILED}, nothing is executed.
     * If this method returns without any exception, the state of
     * this instance is {@link ShaderState#COMPILED}.
     *
     * @return
     *         This Shader object.
     *
     * @throws IllegalStateException
     *         No shader source is set, or this shader has already
     *         been deleted.
     *
     * @throws GLESException
     *         glCompileShader() failed.
     *
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glCompileShader.xml">glCompileShader</a>
     */
    @SuppressWarnings("unchecked")
    public TShader compile() throws GLESException
    {
        switch (state)
        {
            case CREATED:
                // Shader source is not set.
                throw new IllegalStateException("Shader source is not set.");

            case COMPILED:
                // Already compiled.
                return (TShader)this;

            case DELETED:
                // Already deleted.
                throw new IllegalStateException("Shader has already been deleted.");
        }

        // Compile the source code.
        getGLES().glCompileShader(id);

        // Check if the source code has been compiled successfully.
        if (getCompileStatus() == false)
        {
            // Failed to compile the shader source.
            throw new GLESException("glCompileShader() failed: " + getLog());
        }

        // Compiled successfully.
        state = COMPILED;

        return (TShader)this;
    }


    /**
     * Check the compilation status.
     *
     * @return True if the status indicates that compilation
     *          has succeeded.
     */
    private boolean getCompileStatus()
    {
        GLES gles = getGLES();

        int[] status = new int[1];

        // Get the result of compilation.
        gles.glGetShaderiv(id, gles.GL_COMPILE_STATUS(), status, 0);

        // GL_TRUE is returned if the compilation has succeeded.
        return (status[0] == gles.GL_TRUE());
    }


    /**
     * Get the shader information log.
     *
     * @return
     *         A log text.
     *
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetShaderInfoLog.xml">glGetShaderInfoLog</a>
     */
    private String getLog()
    {
        return getGLES().glGetShaderInfoLog(id);
    }


    /**
     * Tell the GLES implementation that resources used by the shader
     * compiler can be released.
     *
     * @see <a href="http://www.khronos.org/opengles/sdk/docs/man/xhtml/glReleaseShaderCompiler.xml">glReleaseShaderCompiler</a>
     */
    public static void releaseCompiler()
    {
        getGLES().glReleaseShaderCompiler();
    }


    /**
     * This method is called when this shader was attached
     * to a {@link Program}.
     *
     * @param program
     *         A {@link Program} that this shader was attached to.
     */
    void onAttached(Program program)
    {
        if (programList != null)
        {
            programList.add(program);
        }
    }


    /**
     * This method is called when this shader was detached
     * from a {@link Program}.
     *
     * @param program
     *         A {@link Program} that this shader was detached from.
     */
    void onDetached(Program program)
    {
        if (programList != null)
        {
            programList.remove(program);

            if (programList.size() == 0 && autoDeleted)
            {
                // This shader is not attached to any program and
                // the flag 'autoDeleted' indicates that this
                // instance should be deleted automatically in
                // such the case.
                delete();
            }
        }
    }


    /**
     * Get an implementation of GLES interface.
     *
     * @return
     *         An object implementing GLES interface.
     */
    private static GLES getGLES()
    {
        return GLESFactory.getInstance();
    }
}
