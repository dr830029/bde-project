/*
 * Pig script to move ready files to a directory
 * input:   hdfs input directory to move
 *          hdfs output directory
 * 
 * 
 */

fs -mv /data/twitch/chat/input/chatdata* /data/twitch/chat/processing/