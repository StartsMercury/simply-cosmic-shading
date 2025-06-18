package io.github.startsmercury.simply_cosmic_shading.impl.client.ignore;

import java.util.Formatter;

@Deprecated(forRemoval = true)
public class ShaderUtil {
    public static String lineNumbered(final String s) {
        final var lines = s.lines().toList();
        final var numPad = Integer.toString(lines.size()).length();
        final var format = new Formatter();
        final var template = "%" + numPad + "d: %s%n";

        var i = 1;
        for (final var line : lines) {
            format.format(template, i++, line);
        }

        return format.toString();
    }

    public static String printLineNumbered(final String s) {
        System.out.println('\n' + lineNumbered(s));
        return s;
    }
}

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME

// TODO RED FLAG REMOVE ME
// FIXME RED FLAG REMOVE ME
