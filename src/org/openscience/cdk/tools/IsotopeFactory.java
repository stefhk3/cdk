/* $RCSfile$
 * $Author$    
 * $Date$    
 * $Revision$
 *
 * Copyright (C) 1997-2002  The Chemistry Development Kit (CDK) project
 *
 * Contact: steinbeck@ice.mpg.de, gezelter@maul.chem.nd.edu, egonw@sci.kun.nl
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.openscience.cdk.tools;

import java.util.*;
import java.io.*;
import org.openscience.cdk.*;

import org.xml.sax.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import JSX.*;

/**
 * Used to store and return data of a particular isotope. As this
 * class is a singleton class, one gets an instance with:
 *
 * <pre>
 * IsotopeFactory ifac = IsotopFactory.getInstance();
 * </pre>
 *
 * @author     steinbeck
 * @created    August 29, 2001
 *
 * @keyword    isotope
 * @keyword    element
 */
public class IsotopeFactory {

    private static IsotopeFactory ifac = null;
    private Vector isotopes = null;

    /**
     * Private constructor for the IsotopeFactory object
     *
     * @exception  IOException             A problem with reading the isotopes.xml file
     * @exception  OptionalDataException   Unexpected data appeared in the isotope ObjectInputStream
     * @exception  ClassNotFoundException  A problem instantiating the isotopes
     * @since
     */
    private IsotopeFactory() throws IOException, OptionalDataException,
                                    ClassNotFoundException {
        InputStream ins = null;
        ObjIn in = null;
        try {
            ins = this.getClass().getClassLoader().getResourceAsStream("org/openscience/cdk/config/isotopes.xml");
        } catch(Exception exc) {
            throw new IOException("There was a problem getting org.openscience.cdk.config.isotopes.xml as a stream");
        }
        if (ins == null) throw new IOException("There was a problem getting org.openscience.cdk.config.isotopes.xml as a stream");
        in = new ObjIn(ins, new Config().aliasID(false));
        isotopes = (Vector) in.readObject();
    }

    /**
     * Returns an IsotopeFactory instance.
     */
    public static IsotopeFactory getInstance() 
        throws IOException, OptionalDataException, ClassNotFoundException 
    {
        if (ifac == null) {
            ifac = new IsotopeFactory();
        }
        return ifac;
    }

    /**
     * Returns the number of isotopes defined by this class.
     */
    public int getSize() {
        return isotopes.size();
    }

    /**
     * Returns the most abundant (major) isotope whose
     * symbol equals element.
     *
     * @param  symbol   Description of Parameter
     * @return          The Major Isotope value
     * @since
     */
    public Isotope getMajorIsotope(String symbol) {
        for (int f = 0; f < isotopes.size(); f++) {
            if (((Isotope)isotopes.elementAt(f)).getSymbol().equals(symbol)) {
                if ((((Isotope)isotopes.elementAt(f))).getNaturalAbundance() == ((double)100)) {
                    return (Isotope)((Isotope)isotopes.elementAt(f)).clone();
                }
            }
        }
        return null;
    }

    /**
     *  Get an array of all isotoptes known to the IsotopeFactory for the given element symbol
     *
     * @param  symbol  An element symbol to search for
     * @return         An array of isotopes that matches the given element symbol
     */
    public Isotope[] getIsotopes(String symbol) {
        ArrayList al = new ArrayList();
        Isotope isotope = null;
        for (int f = 0; f < isotopes.size(); f++) {
            if (((Isotope) isotopes.elementAt(f)).getSymbol().equals(symbol)) {
                al.add((Isotope) ((Isotope) isotopes.elementAt(f)).clone());
            }
        }
        return (Isotope[])al.toArray();
    }


    /**
     *  Returns the major isotope with a given atomic number
     *
     * @param  atomicNumber The atomicNumber for which an isotope is to be returned
     * @return  The isotope corresponding to the given atomic number
     * @since
     */
    public Isotope getMajorIsotope(int atomicNumber) {
        for (int f = 0; f < isotopes.size(); f++) {
            if (((Isotope) isotopes.elementAt(f)).getAtomicNumber() == atomicNumber) {
                if ((((Isotope)    isotopes.elementAt(f))).getNaturalAbundance() == ((double)100)) {
                    return (Isotope) ((Isotope) isotopes.elementAt(f)).clone();
                }
            }
        }
        return null;
    }

    public org.openscience.cdk.Element getElement(String symbol) {
        Isotope i = getMajorIsotope(symbol);
        return (org.openscience.cdk.Element)i;
    }

    public org.openscience.cdk.Element getElement(int atomicNumber) {
        Isotope i = getMajorIsotope(atomicNumber);
        return (org.openscience.cdk.Element)i;
    }

    public Atom configure(Atom atom) {
        Isotope isotope = getMajorIsotope(atom.getSymbol());
        return configure(atom, isotope);
    }

    public Atom configure(Atom atom, Isotope isotope) {
        atom.setAtomicMass(isotope.getAtomicMass());
        atom.setSymbol(isotope.getSymbol());
        atom.setExactMass(isotope.getExactMass());
        atom.setAtomicNumber(isotope.getAtomicNumber());
        atom.setNaturalAbundance(isotope.getNaturalAbundance());
        return atom;
    }

    public void configureAtoms(AtomContainer ac) {
        for (int f = 0; f < ac.getAtomCount(); f++) {
            configure(ac.getAtomAt(f));
        }
    }
}

