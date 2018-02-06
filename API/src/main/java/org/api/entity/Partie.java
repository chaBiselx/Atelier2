/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.api.entity;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author bertrand
 */
@Entity
public class Partie {
    
    @Id
    private String id;
    private String token;
    private int score;
    private Difficulte difficulte;
    @OneToMany
    private Collection<Point> points;
    
}
