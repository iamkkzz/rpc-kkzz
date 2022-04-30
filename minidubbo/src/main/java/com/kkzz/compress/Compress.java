package com.kkzz.compress;

import com.kkzz.extension.Spi;

@Spi
public interface Compress {
    byte[] compress(byte[] bytes);
    byte[] decompress(byte[] bytes);
}
