package com.pradipatle.cityguide.tumsar;
/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class DashboardLayout extends ViewGroup {

   private static final int UNEVEN_GRID_PENALTY_MULTIPLIER = 10;
   private int mMaxChildHeight = 0;
   private int mMaxChildWidth = 0;


   public DashboardLayout(Context var1) {
      super(var1, null);
   }

   public DashboardLayout(Context var1, AttributeSet var2) {
      super(var1, var2, 0);
   }

   public DashboardLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = var4 - var2;
      int var7 = var5 - var3;
      int var8 = this.getChildCount();
      int var9 = 0;

      for(int var10 = 0; var10 < var8; ++var10) {
         if(this.getChildAt(var10).getVisibility() != 8) {
            ++var9;
         }
      }

      if(var9 != 0) {
         int var11 = Integer.MAX_VALUE;
         int var12 = 1;

         int var13;
         int var14;
         int var15;
         while(true) {
            var13 = 1 + (var9 - 1) / var12;
            var14 = (var6 - var12 * this.mMaxChildWidth) / (var12 + 1);
            var15 = (var7 - var13 * this.mMaxChildHeight) / (var13 + 1);
            int var16 = Math.abs(var15 - var14);
            if(var13 * var12 != var9) {
               var16 *= 10;
            }

            if(var16 >= var11) {
               --var12;
               var13 = 1 + (var9 - 1) / var12;
               var14 = (var6 - var12 * this.mMaxChildWidth) / (var12 + 1);
               var15 = (var7 - var13 * this.mMaxChildHeight) / (var13 + 1);
               break;
            }

            var11 = var16;
            if(var13 == 1) {
               break;
            }

            ++var12;
         }

         int var17 = Math.max(0, var14);
         int var18 = Math.max(0, var15);
         int var19 = (var6 - var17 * (var12 + 1)) / var12;
         int var20 = (var7 - var18 * (var13 + 1)) / var13;
         int var21 = 0;

         for(int var22 = 0; var22 < var8; ++var22) {
            View var23 = this.getChildAt(var22);
            if(var23.getVisibility() != 8) {
               int var24 = var21 / var12;
               int var25 = var21 % var12;
               int var26 = var17 * (var25 + 1) + var19 * var25;
               int var27 = var18 * (var24 + 1) + var20 * var24;
               int var28;
               if(var17 == 0 && var25 == var12 - 1) {
                  var28 = var4;
               } else {
                  var28 = var26 + var19;
               }

               int var29;
               if(var18 == 0 && var24 == var13 - 1) {
                  var29 = var5;
               } else {
                  var29 = var27 + var20;
               }

               var23.layout(var26, var27, var28, var29);
               ++var21;
            }
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.mMaxChildWidth = 0;
      this.mMaxChildHeight = 0;
      int var3 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), Integer.MIN_VALUE);
      int var4 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), Integer.MIN_VALUE);
      int var5 = this.getChildCount();

      for(int var6 = 0; var6 < var5; ++var6) {
         View var7 = this.getChildAt(var6);
         if(var7.getVisibility() != 8) {
            var7.measure(var3, var4);
            this.mMaxChildWidth = Math.max(this.mMaxChildWidth, var7.getMeasuredWidth());
            this.mMaxChildHeight = Math.max(this.mMaxChildHeight, var7.getMeasuredHeight());
         }
      }

      int var8 = MeasureSpec.makeMeasureSpec(this.mMaxChildWidth, 1073741824);
      int var9 = MeasureSpec.makeMeasureSpec(this.mMaxChildHeight, 1073741824);

      for(int var10 = 0; var10 < var5; ++var10) {
         View var11 = this.getChildAt(var10);
         if(var11.getVisibility() != 8) {
            var11.measure(var8, var9);
         }
      }

      this.setMeasuredDimension(resolveSize(this.mMaxChildWidth, var1), resolveSize(this.mMaxChildHeight, var2));
   }
}
