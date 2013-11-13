/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.hadoop.hbase.io.hfile;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding;
import org.apache.hadoop.hbase.io.encoding.HFileBlockDecodingContext;
import org.apache.hadoop.hbase.io.encoding.HFileBlockEncodingContext;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Controls what kind of data block encoding is used. If data block encoding is
 * not set or the given block is not a data block (encoded or not), methods
 * should just return the unmodified block.
 */
@InterfaceAudience.Private
public interface HFileDataBlockEncoder {
  /** Type of encoding used for data blocks in HFile. Stored in file info. */
  byte[] DATA_BLOCK_ENCODING = Bytes.toBytes("DATA_BLOCK_ENCODING");

  /**
   * Should be called before an encoded or unencoded data block is written to
   * disk.
   * @param in KeyValues next to each other
   * @param encodingResult the encoded result
   * @param blockType block type
   * @throws IOException
   */
  void beforeWriteToDisk(
    ByteBuffer in,
    HFileBlockEncodingContext encodingResult,
    BlockType blockType
  ) throws IOException;

  /**
   * Decides whether we should use a scanner over encoded blocks.
   * @return Whether to use encoded scanner.
   */
  boolean useEncodedScanner();

  /**
   * Save metadata in HFile which will be written to disk
   * @param writer writer for a given HFile
   * @exception IOException on disk problems
   */
  void saveMetadata(HFile.Writer writer)
      throws IOException;

  /** @return the data block encoding */
  DataBlockEncoding getDataBlockEncoding();

  /**
   * Create an encoder specific encoding context object for writing. And the
   * encoding context should also perform compression if compressionAlgorithm is
   * valid.
   *
   * @param headerBytes header bytes
   * @param fileContext HFile meta data
   * @return a new {@link HFileBlockEncodingContext} object
   */
  HFileBlockEncodingContext newDataBlockEncodingContext(byte[] headerBytes,
      HFileContext fileContext);

  /**
   * create a encoder specific decoding context for reading. And the
   * decoding context should also do decompression if compressionAlgorithm
   * is valid.
   *
   * @param fileContext - HFile meta data
   * @return a new {@link HFileBlockDecodingContext} object
   */
  HFileBlockDecodingContext newDataBlockDecodingContext(HFileContext fileContext);
}
