/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2004-2006  The Chemistry Development Kit (CDK) project
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.cdk.io.formats;

import org.openscience.cdk.tools.DataFeatures;

/**
 * See <a href="http://www.gaussian.com/g_ur/m_input.htm"></a>
 * 
 * @cdk.module io
 * @cdk.set    io-formats
 */
public class Gaussian03Format implements IChemFormatMatcher {

    public Gaussian03Format() {}
    
    public String getFormatName() {
        return "Gaussian 2003";
    }

    public String getMIMEType() {
        return null;
    }
    public String getPreferredNameExtension() {
        return null;
    }
    public String[] getNameExtensions() {
        return new String[0];
    }

    public String getReaderClassName() { 
      return "org.openscience.cdk.io.Gaussian03Reader";
    }
    public String getWriterClassName() { return null; }

    public boolean matches(int lineNumber, String line) {
        if (line.indexOf("Gaussian(R) 03") >= 0) {
            return true;
        }
        return false;
    }

	public boolean isXMLBased() {
		return false;
	}

	public int getSupportedDataFeatures() {
		return DataFeatures.NONE;
	}
}
