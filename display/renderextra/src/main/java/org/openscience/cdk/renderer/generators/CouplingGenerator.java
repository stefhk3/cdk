/*  Copyright (C) 2008  Arvid Berg <goglepox@users.sf.net>
 *
 *  Contact: cdk-devel@list.sourceforge.net
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
package org.openscience.cdk.renderer.generators;

import java.awt.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.vecmath.Point2d;

import org.openscience.cdk.config.Isotopes;
import org.openscience.cdk.geometry.BondTools;
import org.openscience.cdk.geometry.GeometryUtil;
import org.openscience.cdk.graph.ShortestPaths;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.elements.CircularArrowElement;
import org.openscience.cdk.renderer.elements.ElementGroup;
import org.openscience.cdk.renderer.elements.IRenderingElement;
import org.openscience.cdk.renderer.elements.MarkedElement;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator.Scale;
import org.openscience.cdk.tools.ILoggingTool;
import org.openscience.cdk.tools.LoggingToolFactory;
import org.openscience.cdk.tools.manipulator.AtomContainerComparatorBy2DCenter;

/**
 * {@link IGenerator} that can render mass number information of atoms.
 *
 * @cdk.module renderextra
 * @cdk.githash
 */
public class CouplingGenerator implements IGenerator<IAtomContainer> {

    private ILoggingTool logger = LoggingToolFactory.createLoggingTool(CouplingGenerator.class);

	@Override
	public List<IGeneratorParameter<?>> getParameters() {
        return Collections.emptyList();
	}

	@Override
	public IRenderingElement generate(IAtomContainer object, RendererModel model) {
        ElementGroup group = new ElementGroup();
        Map<Color,List<IBond>> couplings=object.getProperty("couplings");
        //we record arcs used several times
        Map<String,List<String>> multiple=new HashMap<>();
        if(couplings!=null){
	        for(Color color : couplings.keySet()){
	        	for(IBond bond : couplings.get(color)){
	        		int factor=3;
	        		if(bond.getAtom(0).getID().compareTo(bond.getAtom(1).getID())<0 && multiple.containsKey(bond.getAtom(0).getID())) {
	        			factor+=multiple.get(bond.getAtom(0).getID()).size();
	        		}
	        		if(bond.getAtom(0).getID().compareTo(bond.getAtom(1).getID())>=0 && multiple.containsKey(bond.getAtom(1).getID())) {
	        			factor+=multiple.get(bond.getAtom(1).getID()).size();
	        		}
	        		//calculate triangle
	                Point2d triangle=new Point2d((bond.getAtom(0).getPoint2d().x + bond.getAtom(1).getPoint2d().x - Math.sqrt(factor)*(bond.getAtom(0).getPoint2d().y-bond.getAtom(1).getPoint2d().y) )/2, (bond.getAtom(0).getPoint2d().y + bond.getAtom(1).getPoint2d().y - Math.sqrt(factor) * (bond.getAtom(1).getPoint2d().x - bond.getAtom(0).getPoint2d().x) )/2);
	        		//calculate centre of bond
	                Point2d centre = new Point2d((bond.getAtom(0).getPoint2d().x+bond.getAtom(1).getPoint2d().x)/2,(bond.getAtom(0).getPoint2d().y+bond.getAtom(1).getPoint2d().y)/2);
	        		//calculate centre between the two
	                Point2d arrowpoint=new Point2d((triangle.x+centre.x)/2,(triangle.y+centre.y)/2);
	                //we look if this coupling is more than one bond
					ShortestPaths sp = new ShortestPaths(object, bond.getAtom(0));
					if(sp.distanceTo(bond.getAtom(1))>1){
						//we look from atom0 to atom1 and see if the centre point and the other atoms on the path are on the same side
						IAtom fakeAtom=object.getBuilder().newAtom();
						fakeAtom.setPoint2d(arrowpoint);
						boolean centreisleft=BondTools.isLeft(fakeAtom, bond.getAtom(0), bond.getAtom(1));
						IAtom[] atomsto=sp.atomsTo(bond.getAtom(1));
						boolean pathisleft=BondTools.isLeft(atomsto[1], bond.getAtom(0), bond.getAtom(1));
						//if so, we put  the centre point to the other side
						if(centreisleft==pathisleft){
			                triangle=new Point2d((bond.getAtom(0).getPoint2d().x + bond.getAtom(1).getPoint2d().x + Math.sqrt(factor)*(bond.getAtom(0).getPoint2d().y-bond.getAtom(1).getPoint2d().y) )/2, (bond.getAtom(0).getPoint2d().y + bond.getAtom(1).getPoint2d().y + Math.sqrt(factor) * (bond.getAtom(1).getPoint2d().x - bond.getAtom(0).getPoint2d().x) )/2);
			                arrowpoint=new Point2d((triangle.x+centre.x)/2,(triangle.y+centre.y)/2);
						}
					}
	        		CircularArrowElement arrow=new CircularArrowElement(bond.getAtom(0).getPoint2d().x, bond.getAtom(0).getPoint2d().y, bond.getAtom(1).getPoint2d().x, bond.getAtom(1).getPoint2d().y, 1 / model.getParameter(
	                        Scale.class).getValue(),arrowpoint.x, arrowpoint.y, color);
	        		group.add(arrow);
	        		if(bond.getAtom(0).getID().compareTo(bond.getAtom(1).getID())<0) {
	        			if(!multiple.containsKey(bond.getAtom(0).getID()))
	        				multiple.put(bond.getAtom(0).getID(),new ArrayList<String>());
	        			multiple.get(bond.getAtom(0).getID()).add(bond.getAtom(1).getID());
	        		}else {
	        			if(!multiple.containsKey(bond.getAtom(1).getID()))
	        				multiple.put(bond.getAtom(1).getID(),new ArrayList<String>());
	        			multiple.get(bond.getAtom(1).getID()).add(bond.getAtom(0).getID());
	        		}
	        	}
	        }
        }
        return group;
	}
}
