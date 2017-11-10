package org.nmdp.hmlfhirconvertermodels.domain.fhir;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 5/26/17.
 * <p>
 * service-hml-fhir-converter-models
 * Copyright (c) 2012-2017 National Marrow Donor Program (NMDP)
 * <p>
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library;  if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.
 * <p>
 * > http://www.fsf.org/licensing/licenses/lgpl.html
 * > http://www.opensource.org/licenses/lgpl-license.php
 */

import org.apache.commons.lang3.StringUtils;
import org.nmdp.hmlfhirconvertermodels.attributes.FhirResource;

import java.math.BigDecimal;

import java.io.Serializable;


@FhirResource
public class Quality implements Serializable {
    private Type code;
    private Sequence standardSequence;
    private Integer start;
    private Integer end;
    private Score score;
    private Method method;
    private BigDecimal truthTP;
    private BigDecimal queryTP;
    private BigDecimal truthFN;
    private BigDecimal queryFP;
    private BigDecimal gtFP;
    private BigDecimal precision;
    private BigDecimal recall;
    private BigDecimal fScore;

    public Type getCode() {
        return code;
    }

    public void setCode(Type code) {
        this.code = code;
    }

    public Sequence getStandardSequence() {
        return standardSequence;
    }

    public void setStandardSequence(Sequence standardSequence) {
        this.standardSequence = standardSequence;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public BigDecimal getTruthTP() {
        return truthTP;
    }

    public void setTruthTP(BigDecimal truthTP) {
        this.truthTP = truthTP;
    }

    public BigDecimal getQueryTP() {
        return queryTP;
    }

    public void setQueryTP(BigDecimal queryTP) {
        this.queryTP = queryTP;
    }

    public BigDecimal getTruthFN() {
        return truthFN;
    }

    public void setTruthFN(BigDecimal truthFN) {
        this.truthFN = truthFN;
    }

    public BigDecimal getQueryFP() {
        return queryFP;
    }

    public void setQueryFP(BigDecimal queryFP) {
        this.queryFP = queryFP;
    }

    public BigDecimal getGtFP() {
        return gtFP;
    }

    public void setGtFP(BigDecimal gtFP) {
        this.gtFP = gtFP;
    }

    public BigDecimal getPrecision() {
        return precision;
    }

    public void setPrecision(BigDecimal precision) {
        this.precision = precision;
    }

    public BigDecimal getRecall() {
        return recall;
    }

    public void setRecall(BigDecimal recall) {
        this.recall = recall;
    }

    public BigDecimal getfScore() {
        return fScore;
    }

    public void setfScore(BigDecimal fScore) {
        this.fScore = fScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quality)) return false;

        Quality quality = (Quality) o;

        if (getCode() != null ? !getCode().equals(quality.getCode()) : quality.getCode() != null) return false;
        if (getStandardSequence() != null ? !getStandardSequence().equals(quality.getStandardSequence()) : quality.getStandardSequence() != null)
            return false;
        if (getStart() != null ? !getStart().equals(quality.getStart()) : quality.getStart() != null) return false;
        if (getEnd() != null ? !getEnd().equals(quality.getEnd()) : quality.getEnd() != null) return false;
        if (getScore() != null ? !getScore().equals(quality.getScore()) : quality.getScore() != null) return false;
        if (getMethod() != null ? !getMethod().equals(quality.getMethod()) : quality.getMethod() != null) return false;
        if (getTruthTP() != null ? !getTruthTP().equals(quality.getTruthTP()) : quality.getTruthTP() != null)
            return false;
        if (getQueryTP() != null ? !getQueryTP().equals(quality.getQueryTP()) : quality.getQueryTP() != null)
            return false;
        if (getTruthFN() != null ? !getTruthFN().equals(quality.getTruthFN()) : quality.getTruthFN() != null)
            return false;
        if (getQueryFP() != null ? !getQueryFP().equals(quality.getQueryFP()) : quality.getQueryFP() != null)
            return false;
        if (getGtFP() != null ? !getGtFP().equals(quality.getGtFP()) : quality.getGtFP() != null) return false;
        if (getPrecision() != null ? !getPrecision().equals(quality.getPrecision()) : quality.getPrecision() != null)
            return false;
        if (getRecall() != null ? !getRecall().equals(quality.getRecall()) : quality.getRecall() != null) return false;
        return getfScore() != null ? getfScore().equals(quality.getfScore()) : quality.getfScore() == null;
    }

    @Override
    public int hashCode() {
        int result = getCode() != null ? getCode().hashCode() : 0;
        result = 31 * result + (getStandardSequence() != null ? getStandardSequence().hashCode() : 0);
        result = 31 * result + (getStart() != null ? getStart().hashCode() : 0);
        result = 31 * result + (getEnd() != null ? getEnd().hashCode() : 0);
        result = 31 * result + (getScore() != null ? getScore().hashCode() : 0);
        result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
        result = 31 * result + (getTruthTP() != null ? getTruthTP().hashCode() : 0);
        result = 31 * result + (getQueryTP() != null ? getQueryTP().hashCode() : 0);
        result = 31 * result + (getTruthFN() != null ? getTruthFN().hashCode() : 0);
        result = 31 * result + (getQueryFP() != null ? getQueryFP().hashCode() : 0);
        result = 31 * result + (getGtFP() != null ? getGtFP().hashCode() : 0);
        result = 31 * result + (getPrecision() != null ? getPrecision().hashCode() : 0);
        result = 31 * result + (getRecall() != null ? getRecall().hashCode() : 0);
        result = 31 * result + (getfScore() != null ? getfScore().hashCode() : 0);
        return result;
    }

    public Boolean hasValue() {
        

        if (getCode() != null && getCode().hasValue()) { return true; }
        if (getStandardSequence() != null && getStandardSequence().hasValue()) { return true; }
        if (getStart() != null && getStart() > 0) { return true; }
        if (getEnd() != null && getEnd() > 0) { return true; }
        if (getScore() != null && getScore().hasValue()) { return true; }
        if (getMethod() != null && getMethod().hasValue()) { return true; }
        if (getTruthFN() != null) { return true; }
        if (getQueryTP() != null) { return true; }
        if (getTruthTP() != null) { return true; }
        if (getQueryFP() != null) { return true; }
        if (getGtFP() != null) { return true; }
        if (getPrecision() != null) { return true; }
        if (getRecall() != null) { return true; }
        if (getfScore() != null) { return true; }

        return false;
    }
}
