class = function(name)
    local mt = {
            __call = function(self,...) 
                        self:__init(...)
                        return self
            end,
            __newindex = function (self,k,v)
                -- create my own __init implementation, 
                -- witch provide 'super' function for original __init
                if(k == '__init') then                     
                    rawset(self,k, function(...)
                                        local tmp = _G['super']
                                        _G['super'] = function(...)
                                                local mt = getmetatable(self)
                                                mt.__index.__init(self,...)
                                        end
                                        -- call original __init function
                                        v(...)
                                        _G['super'] = tmp
                                    end);
                 else
                    rawset(self,k, v); 
                 end
            end
    };

    _G[name] = setmetatable({},mt)

    return function (BaseClass)
        local x = _G[name]
        local mt = getmetatable(x) or {}
        mt.__index = BaseClass
        return setmetatable(x,mt)
    end
end
