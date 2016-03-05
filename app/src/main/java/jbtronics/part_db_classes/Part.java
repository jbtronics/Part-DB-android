/*
 *     Part-DB-android: An Android Barcode Scanner for Part-DB
 *     Copyright (C) 2016 by Jan Boehmer
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jbtronics.part_db_classes;

public class Part {

    //Fields
    private String pid;
    private String name;
    private String description;
    private Integer instock;
    private Integer mininstock;
    private String comment;

    public Part(String pid, String name, String description)
    {
        this.pid = pid;
        this.name = name;
        this.description = description;
    }

    public Part(String data, String separator)
    {
        //Split String into the single values
        String[] datas = data.split(separator);

        for(int n=0;n<datas.length;n++) {

            //Exit if HTML break found
            if (datas[n].contains("<p>")) {
                break;
            }

            datas[n] = datas[n].trim();
            switch (n) {
                case 0:
                    setPid(datas[n]);
                    break;
                case 1:
                    setName(datas[n]);
                    break;
                case 2:
                    setDescription(datas[n]);
                    break;
                case 3:
                    setInstock(datas[n]);
                    break;
                case 4:
                    setMininstock(datas[n]);
                    break;
                case 5:
                    setComment(datas[n]);
                    break;
            }
        }
        
        
    }

    public Part(String data)
    {
        this(data,"@");
    }

    /**
     * Get the Database ID of the Part.
     * @return the ID
     */
    public String getPid() {
        return pid;
    }

    /**
     * Sets the Database ID of the Part
     * @param pid the new ID
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * Gets the Name of the Part
     * @return the name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInstock() {
        return instock;
    }

    public void setInstock(Integer instock) {
        if(instock >= 0) {
            this.instock = instock;
        }
    }

    public void setInstock(String instock)  {
        setInstock(Integer.parseInt(instock.trim()));
    }


    public Integer getMininstock() {
        return mininstock;
    }

    public void setMininstock(Integer mininstock) {
        if(instock >= 0) {
            this.mininstock = mininstock;
        }
    }

    public void setMininstock(String mininstock)
    {
        setMininstock(Integer.parseInt(mininstock.trim()));
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
