<?php

namespace app\Core\Domain\Contract;

use app\Core\Domain\Object\Object;

interface HasEqualsTo
{
    /**
     * @param Object $object
     *
     * @return bool
     */
    public function equalsTo(Object $object) : bool;
}