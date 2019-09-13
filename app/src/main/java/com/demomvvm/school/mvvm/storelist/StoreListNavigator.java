/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version stra2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.demomvvm.school.mvvm.storelist;

import com.demomvvm.school.model.storeListmodel.StoreResponseData;

/**
 * Created by kailash patel on 08/07/17.
 */

public interface StoreListNavigator
{

    void handleError(Throwable throwable);
    void storeResponce(StoreResponseData userResponse);

}
