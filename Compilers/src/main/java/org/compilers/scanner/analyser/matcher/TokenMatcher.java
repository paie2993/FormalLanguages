package org.compilers.scanner.analyser.matcher;

public interface TokenMatcher {

    boolean matches();

    boolean find(final int index);

    int start();

    int end();
}
