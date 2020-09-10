package edu.uwm.cs351;

import java.awt.Graphics;

import javax.swing.JPanel;

public class ManyParticles extends JPanel {

	/**
	 * Put this in to keep Eclipse happy. 
	 */
	private static final long serialVersionUID = 1L;
	
	private final ParticleSeq all;
	
	public ManyParticles(ParticleSeq ps) {
		all = ps;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final ParticleSeq copy = all.clone();
		// TODO: draw all particles in the clone
		// NB: This might run the same time as move()
		// and so we need to use a copy.
		// HINT: you can use p.draw(g) for a particle p
	}
	
	public void move() {
		ParticleSeq copy = all.clone();
		for (all.start(); all.isCurrent(); all.advance()) {
			Vector force = new Vector();
			Particle p = all.getCurrent();
			// System.out.println("Force on " + p);
			for (copy.start(); copy.isCurrent(); copy.advance()) {
				Particle q = copy.getCurrent();
				// System.out.println("  from " + q);
				if (p != q) {
					force = force.add(q.gravForceOn(p));
				}
			}
			p.applyForce(force);
		}
		// TODO: Move all particles
		// HINT: You can use p.move() for a particle p
		repaint();
	}
}
