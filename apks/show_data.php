<?php
/*
    part-db version 0.1
    Copyright (C) 2005 Christoph Lechner
    http://www.cl-projects.de/

    part-db version 0.2+
    Copyright (C) 2009 K. Jacobs and others (see authors.php)
    http://code.google.com/p/part-db/
    
    showData extension
    Copyright (C) 2016 J. Boehmer
    

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
*/

include_once('start_session.php');

$messages = array();
$fatal_error = false; // if a fatal error occurs, only the $messages will be printed, but not the site content

function print_details(Part $p)
{
    try {
        //Get Infos
        $id = strval($p -> get_id());
        $name = $p -> get_name();
        $desc = $p -> get_description();
        $instock = strval($p -> get_instock());
        $mininstock = strval($p -> get_mininstock());
        $comment = $p -> get_comment();
        $obsolete = $p -> get_obsolete();
        $visible = $p -> get_visible();
        $order_quantity = $p -> get_order_quantity();
        $manualorder = $p -> get_manual_order();
        $product_url = $p -> get_manufacturer_product_url();
        $pic_filename = $p -> get_master_picture_filename(true);
        
        $category           = $p->get_category();
        $footprint          = $p->get_footprint();
        $storelocation      = $p->get_storelocation();
        $manufacturer       = $p->get_manufacturer();
        $all_orderdetails   = $p->get_orderdetails();
        
        $category_str = is_object($category) ? $category->get_name() : '';
        $footprint_str = is_object($footprint) ? $footprint->get_name() : '';
        $storelocation_str = is_object($storelocation) ? $storelocation->get_name() : '';
   
        
        //Output Data Format
        print("PID@NAME@DESCRIPTION@INSTOCK@MININSTOCK@COMMENT@CATEGORY@FOOTPRINT@STORELOCATION@<p>\n");
        
        //Print the real Part Data
        print($id . "@" . $name . "@" . $desc . "@" . $instock . "@" . $mininstock . "@" . $comment . "@" . $category_str 
                  . "@" . $footprint_str.  "@" . $storelocation_str . "@<p>\n");
    }
    catch (Exception $e)
    {
        $messages[] = array('text' => nl2br($e->getMessage()), 'strong' => true, 'color' => 'red');
        $fatal_error = true;
        
    }
    
}

    /********************************************************************************
    *
    *   Evaluate $_REQUEST
    *
    *********************************************************************************/


$part_id            = isset($_REQUEST['pid'])               ? (integer)$_REQUEST['pid']             : 0;


//Get Part Details
try
    {
        $database           = new Database();
        $log                = new Log($database);
        $current_user       = new User($database, $current_user, $log, 1); // admin
        $part               = new Part($database, $current_user, $log, $part_id);
        
        
        $footprint          = $part->get_footprint();
        $storelocation      = $part->get_storelocation();
        $manufacturer       = $part->get_manufacturer();
        $category           = $part->get_category();
        $all_orderdetails   = $part->get_orderdetails();
        
        
        print_details($part);
    }
    catch (Exception $e)
    {
        $messages[] = array('text' => nl2br($e->getMessage()), 'strong' => true, 'color' => 'red');
        $fatal_error = true;
        
    }

?>