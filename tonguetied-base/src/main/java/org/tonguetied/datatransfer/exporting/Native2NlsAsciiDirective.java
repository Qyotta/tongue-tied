package org.tonguetied.datatransfer.exporting;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

import freemarker.core.Environment;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class Native2NlsAsciiDirective implements TemplateDirectiveModel {

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException,
            IOException {
        // check validity of call
        // check validity of call
        if (loopVars.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables.");
        }
        if (body == null)
            throw new IllegalArgumentException("missing body");

        boolean isKey = false;
        for (Map.Entry<Object, Object> entry : (Set<Map.Entry<Object, Object>>) params.entrySet()) {
            if ("iskey".equals(entry.getKey())) {
                if (!(entry.getValue() instanceof TemplateBooleanModel)) {
                    throw new TemplateModelException("The \"iskey\" parameter must be a boolean.");
                }
                if (entry.getValue() != null) {
                    isKey = ((TemplateBooleanModel) entry.getValue()).getAsBoolean();
                }
            } else {
                throw new TemplateModelException("Unsupported parameter: " + entry.getKey());
            }
        }

        body.render(new NativeWriter(env.getOut(), isKey));

    }

    static final class NativeWriter extends Writer {
        private static final char BACKSLASH = '\\';
        private final Writer out;
        private final boolean isKey;

        /**
         * Create a new instance of Native2AsciiWriter.
         * 
         * @param out
         */
        NativeWriter(Writer out, final boolean isKey) {
            this.out = out;
            this.isKey = isKey;
        }

        @Override
        public void close() throws IOException {
            out.close();
        }

        @Override
        public void flush() throws IOException {
            out.flush();
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            out.write(cbuf, off, len);
        }
    }
}
