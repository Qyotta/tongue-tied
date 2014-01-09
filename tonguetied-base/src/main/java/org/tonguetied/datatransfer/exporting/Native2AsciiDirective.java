/*
 * Copyright 2008 The Tongue-Tied Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License. 
 */
package org.tonguetied.datatransfer.exporting;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.CharUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Freemarker directive that transforms the output of its nested content from
 * native text to ASCII representation. The content will be escaped to its 
 * unicode characters. 
 * 
 * <p>Parameters:
 * <ul>
 *   <li><code>isKey</code>: flag indicating if this value is a key or entry.
 *   <code>true</code> if it is a key, <code>false</code> otherwise.
 * </ul>
 *
 * <p>Nested content: Yes
 * 
 * @author bsion
 * @see <a href="http://freemarker.sourceforge.net/docs/pgui_datamodel_directive.html">Directives</a>
 */
public class Native2AsciiDirective implements TemplateDirectiveModel
{

    public void execute(Environment env,
            Map params, 
            TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException 
    {
        // check validity of call
        if (loopVars.length != 0)
        {
            throw new TemplateModelException(
                    "This directive doesn't allow loop variables.");
        }
        if (body == null)
            throw new IllegalArgumentException("missing body");
        
        boolean isKey = false;
        for (Map.Entry<Object, Object> entry: (Set<Map.Entry<Object, Object>>) params.entrySet())
        {
            if ("iskey".equals(entry.getKey()))
            {
                if (!(entry.getValue() instanceof TemplateBooleanModel))
                {
                    throw new TemplateModelException(
                            "The \"iskey\" parameter must be a boolean.");
                }
                if (entry.getValue() != null)
                {
                    isKey = ((TemplateBooleanModel) entry.getValue()).getAsBoolean();
                }
            }
            else 
            {
                throw new TemplateModelException(
                        "Unsupported parameter: " + entry.getKey());
            }
        }
        
        body.render(new Native2AsciiWriter(env.getOut(), isKey));
    }

    /**
     * A wrapper around an existing {@linkplain Writer} that transforms the 
     * character stream from native format to ASCII format adhering to the 
     * rules defined for java properties files. 
     * 
     * @author bsion
     * @see http://java.sun.com/j2se/1.5.0/docs/api/java/util/Properties.html
     */
    static final class Native2AsciiWriter extends Writer
    {
        private static final char BACKSLASH = '\\';
        private final Writer out;
        private final boolean isKey;

        /**
         * Create a new instance of Native2AsciiWriter.
         * 
         * @param out
         */
        Native2AsciiWriter(Writer out, final boolean isKey)
        {
            this.out = out;
            this.isKey = isKey;
        }

        @Override
        public void close() throws IOException
        {
            out.close();
        }

        @Override
        public void flush() throws IOException
        {
            out.flush();
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException
        {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < len; i++)
            {
                switch(cbuf[i])
                {
                    case '\t':
                        builder.append(BACKSLASH); 
                        builder.append('t');
                        break;
                    case '\n':
                        builder.append(BACKSLASH); 
                        builder.append('n');
                        break;
                    case '\r':
                        builder.append(BACKSLASH); 
                        builder.append('r');
                        break;
                    case '\f':
                        builder.append(BACKSLASH); 
                        builder.append('f');
                        break;
                    case '\\':
                        builder.append(BACKSLASH);
                        builder.append(BACKSLASH);
                        break;
                    case ' ':
                        if (isKey)
                        {
                            builder.append(BACKSLASH);
                        }
                        builder.append(" ");
                        break;
                    case '\b':
                        builder.append('b');
                        break;
                    default:
                        if (CharUtils.isAsciiPrintable(cbuf[i]))
                        {
                            builder.append(cbuf[i]);
                        }
                        else
                        {
                            builder.append(CharUtils.unicodeEscaped(cbuf[i]));
                        }
                        break;
                }
            }
            
            out.write(builder.toString());
        }
    }
}
