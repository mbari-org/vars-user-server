/*
 * Copyright 2017 Monterey Bay Aquarium Research Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package converters.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import scala.None;
import scala.Option;

import java.lang.reflect.Type;

/**
 * @author Brian Schlining
 * @since 2016-07-12T12:09:00
 */
public class OptionConverter implements JsonSerializer<scala.Option<?>> {

    @Override
    public JsonElement serialize(Option<?> src, Type typeOfSrc, JsonSerializationContext context) {
        if (src.isDefined()) {
            return context.serialize(src.get());
        }
        else {
            return JsonNull.INSTANCE;
        }
    }

}
