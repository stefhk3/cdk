/*
 *  $RCSfile$
 *  $Author$
 *  $Date$
 *  $Revision$
 *
 *  Copyright (C) 2004-2005  The Chemistry Development Kit (CDK) project
 *
 *  Contact: cdk-devel@lists.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.cdk.qsar.descriptors.molecular;

import org.openscience.cdk.interfaces.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.IDescriptor;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;

/**
 * Zagreb index: the sum of the squares of atom degree over all heavy atoms i.
 *
 * @author      mfe4
 * @cdk.created 2004-11-03
 * @cdk.module  qsar
 * @cdk.set     qsar-descriptors
 * @cdk.dictref qsar-descriptors:zagrebIndex
 */
public class ZagrebIndexDescriptor implements IDescriptor {

    /**
     *  Constructor for the ZagrebIndexDescriptor object.
     */
    public ZagrebIndexDescriptor() { }


    /**
     *  Gets the specification attribute of the ZagrebIndexDescriptor object.
     *
     *@return    The specification value
     */
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
            "http://www.blueobelisk.org/ontologies/chemoinformatics-algorithms/#zagrebIndex",
            this.getClass().getName(),
            "$Id$",
            "The Chemistry Development Kit");
    }


    /**
     *  Sets the parameters attribute of the ZagrebIndexDescriptor object.
     *
     *@param  params            The new parameters value
     *@exception  CDKException  Description of the Exception
         *@see #getParameters
     */
    public void setParameters(Object[] params) throws CDKException {
        // no parameters for this descriptor
    }


    /**
     *  Gets the parameters attribute of the ZagrebIndexDescriptor object.
     *
     *@return    The parameters value
         *@see #setParameters
     */
    public Object[] getParameters() {
        return (null);
        // no parameters to return
    }


    /**
     *  Description of the Method.
     *
     *@param  atomContainer                AtomContainer
     *@return                   zagreb index
     *@exception  CDKException  Possible Exceptions
     */
    public DescriptorValue calculate(AtomContainer atomContainer) throws CDKException {
        double zagreb = 0;
        org.openscience.cdk.interfaces.Atom[] atoms = atomContainer.getAtoms();
        for (int i = 0; i < atoms.length; i++) {
            int atomDegree = 0;
            org.openscience.cdk.interfaces.Atom[] neighboors = atomContainer.getConnectedAtoms(atoms[i]);
            for (int a = 0; a < neighboors.length; a++) {
                if (!neighboors[a].getSymbol().equals("H")) {
                    atomDegree += 1;
                }
            }
            zagreb += (atomDegree * atomDegree);
        }
        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), new DoubleResult(zagreb));
    }


    /**
     *  Gets the parameterNames attribute of the ZagrebIndexDescriptor object.
     *
     *@return    The parameterNames value
     */
    public String[] getParameterNames() {
        // no param names to return
        return (null);
    }



    /**
     *  Gets the parameterType attribute of the ZagrebIndexDescriptor object.
     *
     *@param  name  Description of the Parameter
     *@return       The parameterType value
     */
    public Object getParameterType(String name) {
        return (null);
    }
}

