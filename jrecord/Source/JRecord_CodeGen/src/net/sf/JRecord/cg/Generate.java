/*  -------------------------------------------------------------------------
 *
 *            Sub-Project: JRecord CodeGen
 *    
 *    Sub-Project purpose: Generate Java - JRecord source code 
 *                        to read/write cobol data files.
 *    
 *                 Author: Bruce Martin
 *    
 *                License: GPL
 *                
 *    Copyright (c) 2016, Bruce Martin, All Rights Reserved.
 *   
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *   
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 * ------------------------------------------------------------------------ */
      
package net.sf.JRecord.cg;

import net.sf.JRecord.cg.details.GenerateOptions;
import net.sf.JRecord.cg.details.ParseArgs;
import net.sf.JRecord.cg.velocity.GenerateVelocity;

public class Generate {

	public static void main(String[] args) {
		ParseArgs pa = new ParseArgs(args);

		if (pa.get2Args("-h", "-help", pa.getArg("-?")) != null) {
			GenerateOptions.printOptions();
		} else {
			GenerateOptions opts = new GenerateOptions(pa);
			
			if (opts.isOk()) {
				new GenerateVelocity( opts);
			}
		}

	}

}